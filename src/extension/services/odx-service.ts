import * as child_process from "child_process";
import * as fs from 'fs';
import * as net from 'net';
import * as path from 'path';
import * as vscode from 'vscode';
import { LanguageClient, LanguageClientOptions, StreamInfo } from 'vscode-languageclient/node';
import { DiagService, DiagnosticElement, Document, Reference, LayerDetails, StartTagLocation } from '../../shared/models';

/**
 * This class implements all client related LSP services
 */
export class OdxLspService {
    private lspClient: LanguageClient;

    constructor(context: vscode.ExtensionContext) {
        this.lspClient = this.startLSP(context);
        vscode.workspace.onDidChangeConfiguration((event) => {
            if (event.affectsConfiguration("odx-server.activeIndexLocation")) {
                this.sendConfigurationChanged();
            }
        });
        this.initNotifications();
    }

    private initNotifications() {
        this.lspClient.onReady().then(() =>
            //reload data whenever the global index changed
            this.lspClient.onNotification("odx/indexChanged", () => {
                vscode.commands.executeCommand("odx.reloadData");
            })
        );
    }

    public async sendConfigurationChanged() {
        const indexLocation = vscode.workspace.getConfiguration().get("odx-server.activeIndexLocation") as string;
        if (indexLocation) {
            await this.lspClient.onReady();
            this.lspClient.sendNotification("workspace/didChangeConfiguration", {});
            vscode.commands.executeCommand("odx.reloadData");
        }
    }

    public async fetchServiceDetails(service: DiagService) {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getServiceDetails", service);
        return result as DiagnosticElement[];
    }


    public async fetchLayerDetails(reference: Reference) {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getLayerDetails", reference);
        return result as LayerDetails;
    }

    public async fetchCategoriesByType(type: string): Promise<Document[]> {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getCategoriesByType", { value: type });
        return result as Document[];
    }

    public async fetchCategoryDetails(reference: Reference): Promise<DiagnosticElement[]> {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getCategoryDetails", reference);
        return result as DiagnosticElement[];
    }


    public async fetchDiagnosticLayers(type: string): Promise<Document[]> {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getLayersByType", { value: type });
        return result as Document[];
    }

    public async fetchDocument(uri: vscode.Uri): Promise<string> {
        await this.lspClient.onReady();
        const result = await this.lspClient.sendRequest("odx/getContent", { value: uri.toString() });
        return result as string;
    }

    private startLSP(context: vscode.ExtensionContext) {
        const executablePath = context.asAbsolutePath(path.join('resources', 'odx-language-server.jar'));

        let clientOptions: LanguageClientOptions = {
            documentSelector: [{ scheme: 'odx', language: 'odx' }, { scheme: 'file', language: 'odx' }],
            synchronize: {
                fileEvents: vscode.workspace.createFileSystemWatcher('**/*.{odx,pdx}*')
            },
            markdown: {
                isTrusted: true
            }

        };

        const serverOptions = executablePath && fs.existsSync(executablePath) ? this.startServer(executablePath) : this.connectToClient;

        const client = new LanguageClient(
            'odx-server',
            'ODX Language Server',
            serverOptions,
            clientOptions
        );

        client.start();

        return client;
    }

    private startServer(executablePath: string): () => Promise<StreamInfo> {
        return () => new Promise((resolve) => {
            const server = net.createServer(socket =>
                resolve({
                    reader: socket,
                    writer: socket
                })
            ).listen(0, "127.0.0.1", () => {
                const port = (server.address() as net.AddressInfo).port;
                const command = "java -XX:+UseStringDeduplication -Xmx" + this.getHeapSpaceConfiguration() + " -jar \"" + executablePath + "\" " + port;
                child_process.exec(command, (error, stdout, stderr) => {/** TODO log errors */ });
            });
        });
    };

    private getHeapSpaceConfiguration() {
        return vscode.workspace.getConfiguration().get('odx-server.maxHeapSpace', '1g').toLowerCase();
    }

    private connectToClient(): Promise<StreamInfo> {
        let socket = net.connect({ port: 8090 });
        let result: StreamInfo = {
            writer: socket,
            reader: socket
        };
        return Promise.resolve(result);
    };

}


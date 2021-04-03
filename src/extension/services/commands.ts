import * as vscode from 'vscode';
import { LanguageClient } from 'vscode-languageclient';
import { Location } from '../../shared/models';
import { OdxLspService } from './odx-service';

export const jumpToLineHandler = (location: Location, preview: boolean) => {
	const startPosition = new vscode.Position(location.startLine, 0);
	const endPosition = new vscode.Position(location.startLine + 1, 0);
	const prefix = location.fileUri.startsWith("jar:") ? "odx:" : "";
	vscode.commands.executeCommand("vscode.open", vscode.Uri.parse(prefix + location.fileUri), { selection: new vscode.Range(startPosition, endPosition), preserveFocus: true, preview: preview });
};

export const resolveLinkHandler = (odxService: OdxLspService) => async (uri: vscode.Uri, linkId: number) => {
	const location = await odxService.resolveLink(uri, linkId);
	if (location) {
		jumpToLineHandler(location, false);
	}
};

export const odxSourceHandler = async (odxSource: vscode.Uri) => {
	//reset and set to enforce configuration change
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", undefined, true);
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", odxSource.fsPath, true);
};
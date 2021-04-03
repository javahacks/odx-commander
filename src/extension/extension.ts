import * as vscode from 'vscode';
import { jumpToLineHandler, odxSourceHandler, resolveLinkHandler } from './services/commands';
import { initDiagDataProvider } from './services/diag-data-provider';

import { OdxLspService } from './services/odx-service';
import { OdxTextDocumentProvider } from './services/textdocument-provider';

/**
 * Called when the odx extension is activated
 * 
 * @param context 
 */
export function activate(context: vscode.ExtensionContext) {
	const odxService = new OdxLspService(context);

	context.subscriptions.push(vscode.commands.registerCommand('odx.jumpToLine', jumpToLineHandler));
	context.subscriptions.push(vscode.commands.registerCommand("odx.setOdxSource", odxSourceHandler));
	context.subscriptions.push(vscode.commands.registerCommand("odx.resolveLink", resolveLinkHandler(odxService)));

	initDiagDataProvider(context, odxService);

	vscode.workspace.registerTextDocumentContentProvider("odx", new OdxTextDocumentProvider(odxService));
}




export function deactivate() { }



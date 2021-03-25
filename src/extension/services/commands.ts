import * as vscode from 'vscode';
import { Location } from '../../shared/models';

export const jumpToLineHandler = (location: Location, preview: boolean) => {
	const startPosition = new vscode.Position(location.startLine, 0);
	const endPosition = new vscode.Position(location.startLine, location.startColumn);
	vscode.commands.executeCommand("vscode.open", vscode.Uri.parse("odx:" + location.fileUri), { selection: new vscode.Range(startPosition, endPosition), preserveFocus: true, preview: preview });
};

export const odxSourceHandler = async (odxSource: vscode.Uri) => {
	//reset and set to enforce configuration change
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", undefined, true);
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", odxSource.fsPath, true);
};
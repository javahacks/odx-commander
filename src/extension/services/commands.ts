import * as vscode from 'vscode';

export const jumpToLineHandler = (uri: string, startLine: number, startColumn: number, endLine: number, endColumn: number) => {
	const startPosition = new vscode.Position(startLine, 0);
	const endPosition = new vscode.Position(endLine, endColumn);

	vscode.commands.executeCommand("vscode.open", vscode.Uri.parse("odx:"+uri), {  selection: new vscode.Range(startPosition, endPosition) ,preserveFocus:true});
};

export const odxSourceHandler = async (odxSource: vscode.Uri) => {
	//reset and set to enforce configuration change
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", undefined, true);
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", odxSource.fsPath, true);
};
import * as vscode from 'vscode';

export const jumpToLineHandler = (uri: string, startLine: number, startColumn: number, endLine: number, endColumn: number) => {
	const startPosition = new vscode.Position(startLine, 0);
	const endPosition = new vscode.Position(endLine, endColumn);
	vscode.commands.executeCommand("vscode.open", vscode.Uri.parse(uri), { selection: new vscode.Range(startPosition, endPosition) });
};

export const odxSourceHandler = async (odxSource: vscode.Uri) => {
	await vscode.workspace.getConfiguration().update("odx-server.activeIndexLocation", odxSource.fsPath, true);
};
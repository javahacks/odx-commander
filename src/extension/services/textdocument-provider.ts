import * as vscode from 'vscode';
import { OdxLspService } from './odx-service';

/**
 * Used to load virtual ODX documents from within PDX files or to provide readonly behaviour from filesystem.
 */
export class OdxTextDocumentProvider implements vscode.TextDocumentContentProvider {

	constructor(private odxService: OdxLspService) {
		this.odxService = odxService;
	}

	public provideTextDocumentContent(uri: vscode.Uri): Promise<string> {
		return this.odxService.fetchDocument(uri);
	};
}



import * as path from 'path';
import * as vscode from 'vscode';
import { Document, DiagnosticElement as DiagnosticElement, BaseObject, DiagService as DiagService } from '../../shared/models';
import { OdxLspService } from './odx-service';

export function initDiagDataProvider(context: vscode.ExtensionContext, odxService: OdxLspService) {

  const diagnosticProviderMap = new Map<string, RefreshableBaseNodeProvider>();

  diagnosticProviderMap.set('protocol', new LayerDataTreeProvider("protocol", odxService, context));
  diagnosticProviderMap.set('functional_group', new LayerDataTreeProvider("functional_group", odxService, context));
  diagnosticProviderMap.set('shared_data', new LayerDataTreeProvider("shared_data", odxService, context));
  diagnosticProviderMap.set('base_variant', new LayerDataTreeProvider("base_variant", odxService, context));
  diagnosticProviderMap.set('ecu_variant', new LayerDataTreeProvider("ecu_variant", odxService, context));

  diagnosticProviderMap.set('flash', new CategoryTreeProvider("flash", odxService, context));
  diagnosticProviderMap.set('vehicle_info_spec', new CategoryTreeProvider("vehicle_info_spec", odxService, context));
  diagnosticProviderMap.set('multiple_ecu_job_spec', new CategoryTreeProvider("multiple_ecu_job_spec", odxService, context));
  diagnosticProviderMap.set('comparam_spec', new CategoryTreeProvider("comparam_spec", odxService, context));
  diagnosticProviderMap.set('comparam_subset', new CategoryTreeProvider("comparam_subset", odxService, context));
  diagnosticProviderMap.set('ecu_config', new CategoryTreeProvider("ecu_config", odxService, context));
  diagnosticProviderMap.set('function_dictionary_spec', new CategoryTreeProvider("function_dictionary_spec", odxService, context));

  diagnosticProviderMap.forEach((value, key) => {
    vscode.window.registerTreeDataProvider(key, value);
  });

  //reload visible tree views on model change  
  context.subscriptions.push(vscode.commands.registerCommand("odx.reloadData", () =>    
    diagnosticProviderMap.forEach((value, key) => value.refresh())
  ));

}

abstract class RefreshableBaseNodeProvider implements vscode.TreeDataProvider<BaseNode>{
  constructor(protected type: string, protected odxService: OdxLspService, protected context: vscode.ExtensionContext) {
  }
  getTreeItem(element: BaseNode): BaseNode | Thenable<BaseNode> {
    return element;
  }
  public getChildren(element?: BaseNode): vscode.ProviderResult<vscode.TreeItem[]> {
    if (element instanceof DiagnosticElementNode && element.item.children) {
      return Promise.resolve(element.item.children.map(service => new DiagnosticElementNode(service, this.context)));
    }
    return Promise.resolve([]);
  }

  private changeEmitter: vscode.EventEmitter<BaseNode | undefined | null | void> = new vscode.EventEmitter<BaseNode | undefined | null | void>();
  readonly onDidChangeTreeData: vscode.Event<BaseNode | undefined | null | void> = this.changeEmitter.event;

  refresh(): void {
    this.changeEmitter.fire();
  }
}

/**
 * Tree provider for diagnostic layers
 */
class LayerDataTreeProvider extends RefreshableBaseNodeProvider {

  public getChildren(element?: BaseNode): vscode.ProviderResult<vscode.TreeItem[]> {
    if (element instanceof ServiceNode) {
      const details = this.odxService.fetchServiceDetails(element.service);
      return details.then(details => details.map(detail => new DiagnosticElementNode(detail, this.context)));
    }

    if (element instanceof GroupingNode) {
      return element.children;
    }

    if (element instanceof DocumentNode) {
      return this.getDocumentChildren(element);
    }

    if (!element) {
      const layers = this.odxService.fetchDiagnosticLayers(this.type);
      return layers.then(layers => layers.map(layer => new DocumentNode(layer, this.type, this.context)));
    }

    return super.getChildren(element);
  }



  private getDocumentChildren(element: DocumentNode) {

    const layerDetails = this.odxService.fetchLayerDetails(element.layer.odxLink);

    return layerDetails.then(layerDetails => {
      const children = [] as vscode.TreeItem[];
      if (layerDetails.services.length > 0) {
        const childNodes = layerDetails.services.map(service => new ServiceNode(service, this.context));
        children.push(new GroupingNode("Diagnostic Services", childNodes, this.context, 'service.svg'));
      }
      if (layerDetails.variantPatterns && layerDetails.variantPatterns.length > 0) {
        const variantPattern = layerDetails.variantPatterns.map(pattern => new DiagnosticElementNode(pattern, this.context));
        children.push(new GroupingNode("Variant Patterns", variantPattern, this.context, 'variant_pattern.svg'));
      }
      
      if (layerDetails.dependencies && layerDetails.dependencies.length > 0) {
        const dependencies = layerDetails.dependencies.map(pattern => new DiagnosticElementNode(pattern, this.context));
        children.push(new GroupingNode("Dependencies", dependencies, this.context, 'dependencies.svg'));
      }

      return children;

    });
  }
}

/**
 * Tree provider for simple ODX Categories
 */
class CategoryTreeProvider extends RefreshableBaseNodeProvider {
  getChildren(element?: BaseNode): vscode.ProviderResult<vscode.TreeItem[]> {
    if (element instanceof DocumentNode) {
      const details = this.odxService.fetchCategoryDetails(element.layer.odxLink);
      return details.then(details => details.map(object => new DiagnosticElementNode(object, this.context)));
    }
    if (!element) {
      const categories = this.odxService.fetchCategoriesByType(this.type);
      return categories.then(layers => layers.map(category => new DocumentNode(category, this.type, this.context)));
    }
    return super.getChildren(element);
  }
}


class BaseNode extends vscode.TreeItem {
  constructor(object: BaseObject, context: vscode.ExtensionContext) {
    super(object.name);
    this.collapsibleState = vscode.TreeItemCollapsibleState.Collapsed;
    const location = object.location;
    if (location) {
      this.command = { command: "odx.jumpToLine", title: "Open location", arguments: [location, true] };
    }
  }
}

class DocumentNode extends BaseNode {
  constructor(public layer: Document, type: string, context: vscode.ExtensionContext) {
    super(layer, context);
    this.collapsibleState = layer.expandable ? vscode.TreeItemCollapsibleState.Collapsed : vscode.TreeItemCollapsibleState.None;
    this.iconPath = context.asAbsolutePath(path.join('resources', 'media', 'documents', `${type.toLowerCase()}.svg`));
  }
}


class GroupingNode extends vscode.TreeItem {
  constructor(name: string, public children: BaseNode[], context: vscode.ExtensionContext, icon?: string,) {
    super(name);
    this.collapsibleState = children.length > 0 ? vscode.TreeItemCollapsibleState.Collapsed : vscode.TreeItemCollapsibleState.None;
    if (icon) {
      this.iconPath = context.asAbsolutePath(path.join('resources', 'media', 'types', icon));
    }
  }
}

class ServiceNode extends BaseNode {
  constructor(public service: DiagService, context: vscode.ExtensionContext) {
    super(service, context);
    this.iconPath = context.asAbsolutePath(path.join('resources', 'media', 'types', `${service.type.toLowerCase()}.svg`));
  }
}

class DiagnosticElementNode extends BaseNode {
  constructor(public item: DiagnosticElement, context: vscode.ExtensionContext) {
    super(item, context);
    this.collapsibleState = item.children && item.children.length > 0 ? vscode.TreeItemCollapsibleState.Collapsed : vscode.TreeItemCollapsibleState.None;
    if (item.type) {
      this.iconPath = context.asAbsolutePath(path.join('resources', 'media', 'types', `${item.type.toLowerCase()}.svg`));
    }
  }
}
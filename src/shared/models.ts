export interface BaseObject {
  name: string;
  location: StartTagLocation
  type: string
}

export interface Document extends BaseObject {
  odxLink: Reference;
  expandable: boolean;
}

export interface DiagService extends BaseObject {
  shortName: string;
  odxLink: Reference;
}

export interface DiagnosticElement extends BaseObject {
  children?: DiagnosticElement[]
  parentName?: string
}

export interface LayerDetails {
  services: DiagService[]
  variantPatterns?: DiagnosticElement[]
  dependencies?: DiagnosticElement[]
}

export interface StartTagLocation {
  fileUri: string;
  startLine: number
  startColumn: number  
}

export interface Reference {
  docType: string;
  docRef: string;
  idRef: string;
}
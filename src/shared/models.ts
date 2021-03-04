export interface BaseObject {
  name: string;
  location: Location
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
}

export interface LayerDetails {
  services: DiagService[]
  variantPatterns?: DiagnosticElement[]
}

export interface Location {
  fileUri: string;
  startLine: number
  startColumn: number
  endLine: number
  endColumn: number
}

export interface Reference {
  docType: string;
  docRef: string;
  idRef: string;
}
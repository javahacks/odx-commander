export interface BaseObject {
  label?: string
  name: string;
  type: string
  location: StartTagLocation
}

export interface Document extends BaseObject {
  odxLink: Reference;
  expandable: boolean;
}

export interface DiagService extends BaseObject {  
  odxLink: Reference;
}

export interface DiagnosticElement extends BaseObject {
  children?: DiagnosticElement[] 
  revealable: boolean
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
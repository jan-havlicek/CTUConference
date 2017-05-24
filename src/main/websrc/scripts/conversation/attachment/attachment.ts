
export class Attachment {

	private _id: number;
	private _file: File;
	private _data: ArrayBuffer;
	private _dataInUInt8Array: Uint8Array;
	private _name: string;
	private _contentType: string;
	private _downloadUrl: string;
	private _slideshowActive: boolean;
	private _initialPage: number

	static fromJson(jsonObject: any): Attachment {
		var attachment = new Attachment();
		attachment.name = jsonObject.fileName;
		attachment.contentType = jsonObject.contentType;
		attachment.downloadUrl = jsonObject.downloadUrl;
		return attachment;
	}

	constructor(name?: string, contentType?: string, data?: ArrayBuffer, file: File = null) {
		this._name = name;
		this._contentType = contentType;
		this._data = data;
		this._file = file;
		this._slideshowActive = false;
	}

	get isDownloaded() {
		return this.data &&this.data.byteLength > 0;
	}

	get isPresentation() {
		return this._contentType == "application/pdf" || this._name.match(/\.pdf$/);
	}

	get isPresentable() {
		return [
			"application/vnd.oasis.opendocument.presentation",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation",
			"application/pdf"
			].indexOf(this.contentType) !== -1 || (
				this._name.match(/(\.pdf)|(\.odp)|(\.pptx)$/)
			);
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get contentType(): string {
		return this._contentType;
	}

	set contentType(value: string) {
		this._contentType = value;
	}

	get name(): string {
		return this._name;
	}

	set name(value: string) {
		this._name = value;
	}

	get data(): ArrayBuffer {
		return this._data;
	}

	set data(value: ArrayBuffer) {
		this._data = value;
	}

	get size(): string {
		if(this._data.byteLength < 1000) {
			return this._data.byteLength + " B";
		} else if(this._data.byteLength < 1000000) {
			return (Math.floor(this._data.byteLength / 100) / 10) + " kB";
		} else {
			return (Math.floor(this._data.byteLength / 100000) / 10) + " MB";
		}
	}

	set downloadUrl(value: string) {
		this._downloadUrl = value;
	}

	get downloadUrl(): string {
		return this._downloadUrl;
	}

	get file(): File {
		return this._file;
	}

	set file(value: File) {
		this._file = value;
	}

	toUInt8Array(): Uint8Array {
		if(!this._dataInUInt8Array) {
			this._dataInUInt8Array = new Uint8Array(this._data);
		}
		return this._dataInUInt8Array;
	}

	toUInt16Array(): Uint16Array {
		return new Uint16Array(this._data);
	}

	get slideshowActive(): boolean {
		return this._slideshowActive;
	}

	set slideshowActive(value: boolean) {
		this._slideshowActive = value;
	}


	get initialPage(): number {
		return this._initialPage || 0;
	}

	set initialPage(value: number) {
		this._initialPage = value;
	}
}

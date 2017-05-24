import {Injectable} from "@angular/core";
import {Attachment} from "./attachment";
import {Subject} from "rxjs";

@Injectable()
export class FileService {

	loadFile(file: File): Promise<Attachment> {
		return new Promise((resolve, reject) => {
			let reader = new FileReader();
			reader.onload = (event) => {
				let attachment = new Attachment(file.name, file.type, reader.result, file);
				resolve(attachment);
				//this._attachment$.next(new Attachment(file, reader.result))
			}
			reader.onerror = function (e) {
				reject(e);
			};
			reader.readAsArrayBuffer(file);
		});
	}
}

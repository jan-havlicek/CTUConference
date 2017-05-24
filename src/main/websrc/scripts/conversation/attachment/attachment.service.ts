import {Injectable} from "@angular/core";
import {Attachment} from "./attachment";
import {Subject} from "rxjs";
import {FileService} from "./file.service";
import {Http} from "@angular/http";
import {ApiService} from "../../utils/api.service";

@Injectable()
export class AttachmentService {

	constructor(
		private fileService: FileService,
		private apiService: ApiService
	) {

	}

	private _attachment$: Subject<Attachment>;

	uploadAttachment(attachment: Attachment) {

	}

	downloadAttachment(attachment: Attachment): Promise<Attachment> {
		return new Promise<Attachment>((resolve, reject) => {
			this.apiService.getFile("/CTUConference" + attachment.downloadUrl)
				.then(_attachment => {
					attachment.data = _attachment.data;
					resolve(attachment);
				}).catch(err => reject(err));
		});
		// this.apiService.getFile(attachment.downloadUrl).then(newAttachment => {
		// 	attachment.data = newAttachment.data;
		// });
	}

}

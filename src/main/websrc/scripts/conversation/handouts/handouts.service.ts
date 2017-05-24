import {Injectable} from "@angular/core";
import {Subject, Observable} from "rxjs";
import {SocketMessageService} from "../../communication/socket-message.service";
import {FileService} from "../attachment/file.service";
import {Attachment} from "../attachment/attachment";
import {ApiService} from "../../utils/api.service";
import {SocketMessage} from "../../communication/socket-message";

@Injectable()
export class HandoutsService {

	/**
	 * Observable of handouts document (next when new slides are uploaded
	 */
	private _handouts$: Subject<Attachment>;

	/**
	 * Observable of page change
	 */
	private _handoutsUpdates$: Subject<number>;

	//private downloadingHandoutsInitialPage: number;

	constructor(
		private fileService: FileService,
		private messageService: SocketMessageService,
		private apiService: ApiService
	) {
		this._handoutsUpdates$ = <Subject<number>>new Subject();
		this._handouts$ = <Subject<Attachment>>new Subject();
		this.messageService.subscribeToDataStream("handouts.update-page", result => {
			//this.downloadingHandoutsInitialPage = result.data.page;
			this._handoutsUpdates$.next(result.data.page);
		});
		this.messageService.subscribeToDataStream("handouts.added", result => {
			//this.downloadingHandoutsInitialPage = result.data.currentPage;
			let handouts = new Attachment("", "application/pdf");
			handouts.initialPage = result.data.currentPage
			handouts.downloadUrl = "/CTUConference/api/conversation/" + result.data.conversationId+"/handouts";
			this._handouts$.next(handouts);
			//this.downloadHandouts(result.data.conversationId);
		});
	}

	setHandouts(file: File, conversationId: number, persistent: boolean) {
		this.fileService.loadFile(file).then(attachment => {
			//@todo if there are possible any other formats, it is necessary to format them and download first
			this._handouts$.next(attachment);
			this.uploadHandouts(attachment, conversationId, persistent);
			//this.messageService.send(new SocketMessage("handouts.set", {id: 1, data: attachment.toUInt8Array()}));
		});
	}

	/**
	 * There is not handouts id necessary, because there is only one handouts
	 * that is necessary to synchronize.
	 */
	get handoutsUpdates$(): Observable<number> {
		return this._handoutsUpdates$.asObservable();
	}

	get handouts$(): Observable<Attachment> {
		return this._handouts$.asObservable();
	}

	uploadHandouts(attachment: Attachment, conversationId: number, persistent: boolean = true) {
		let url = `/CTUConference/api/conversation/${conversationId}/handouts${persistent ? "/persistent" : ""}`;
		this.apiService.putFile(url, attachment);
	}

	synchronizePage(page: number) {
		let message = new SocketMessage("handouts.update-page", {
			page: page
		})
		this.messageService.send(message);
	}
    //
	// private downloadHandouts(conversationId: number): void {
	// 	let url = `/CTUConference/api/conversation/${conversationId}/handouts`;
	// 	let promise = this.apiService.getFile(url);
	// 	promise.then(attachment => {
	// 		this._handouts$.next(attachment);
	// 		this._handoutsUpdates$.next(this.downloadingHandoutsInitialPage);
	// 	});
	// }
}

import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Attachment} from "../conversation/attachment/attachment";
import {toPromise} from "rxjs/operator/toPromise";

@Injectable()
export class ApiService {

	private BASE_PATH = "/CTUConference";

	constructor(
		private http: Http
	) {
	}

	/**
	 * Send JSON message to the server
	 * @param url
	 * @param data
	 * @returns {Observable<R>}
	 */
	postJson(url: string, data): Observable<any> {
		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers });
		let dataString = JSON.stringify(data);
		return this.http.post(this.BASE_PATH + url, data, options)
			.map((res: Response) => res.json());
	}

	/**
	 * Get JSON message from the server on the defined URL
	 * @param url
	 * @returns {Observable<R>}
	 */
	getJson(url: string): Observable<any> {
		return this.http.get(this.BASE_PATH + url)
			.map((res: Response) => res.json());
	}

	/**
	 * Get file from the server and transform it to Attachment object
	 * @param url
	 * @returns {Promise<Attachment>}
	 */
	getFile(url: string): Promise<Attachment> {
		return this.http.get(url).map((res: Response) => {
			let fileName = res.headers.get("Content-Disposition").replace(/^.*filename="([^"]+)".*$/, "$1");
			let contentType = res.headers.get("Content-Type").replace(/^([^;]+);.*$/, "$1");
			let attachment = new Attachment(fileName, contentType, res.arrayBuffer());
			return attachment;
		}).toPromise();
	}

	/**
	 * Send file to the server.
	 * @param url
	 * @param attachment
	 * @returns {Promise<R>}
	 */
	putFile(url, attachment: Attachment): Promise<string> {
		let formData = new FormData();
		formData.append("file", attachment.file);
		return this.http.post(url, formData)
			.map((res: Response) => res.headers.get("Location")).toPromise();
		// @todo there is not always location
	}
}


export class User {
	private _id: number;
	private _avatar: string;
	private _email: string;
	private _firstName: string;
	private _lastName: string;
	private _isActive: boolean;

	static fromJson(jsonObject) {
		let user = new User();
		user.id = jsonObject.id;
		user.avatar = jsonObject.avatar;
		user.firstName = jsonObject.firstName;
		user.lastName = jsonObject.lastName;
		user.isActive = jsonObject.isActive;
		user.email = jsonObject.email;
		return user;
	}
	get fullName(): string {
		return this._firstName + " " + this._lastName;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get avatar(): string {
		return this._avatar;
	}

	set avatar(value: string) {
		this._avatar = value;
	}

	get email(): string {
		return this._email;
	}

	set email(value: string) {
		this._email = value;
	}

	get firstName(): string {
		return this._firstName;
	}

	set firstName(value: string) {
		this._firstName = value;
	}

	get lastName(): string {
		return this._lastName;
	}

	set lastName(value: string) {
		this._lastName = value;
	}

	get isActive(): boolean {
		return this._isActive;
	}

	set isActive(value: boolean) {
		this._isActive = value;
	}
}

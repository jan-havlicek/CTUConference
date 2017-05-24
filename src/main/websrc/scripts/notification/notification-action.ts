export interface NotificationAction {
	title: string;
	action: (option?: any) => void;
	options?: {label: string, value: string|number}[];
}

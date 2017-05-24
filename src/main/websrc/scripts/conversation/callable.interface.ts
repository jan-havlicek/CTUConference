export interface Callable {
	isVoiceCallPermitted(): boolean;
	isVideoCallPermitted(): boolean;
	isWebinarPermitted(): boolean;
}

declare module "hark" {
	interface hark {
		(stream: any, options: {interval?: number, threshold?: number, play?: boolean}): {
			speaking: boolean;
			on(event: string, callable: () => void): void;
			setThreshold(threshold: number): void;
			setInterval(interval: number): void;
			stop(): void;
			speakingHistory: number[];
		};
	}

	var harker: hark;

	export = harker;
}

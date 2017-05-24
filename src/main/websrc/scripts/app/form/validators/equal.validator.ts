import {FormControl} from "@angular/forms";

/**
 *
 * @param controlName
 * @returns {(control:FormControl)=>({validateEqual: boolean}|null)}
 */
export function validateEqual(controlName: string) {
	return (control: FormControl) => {
		let value = control.value;
		let compareElement = control.root.get(controlName);
		if (compareElement && value !== compareElement.value) return {
			validateEqual: false
		}
		return null;
	}
}

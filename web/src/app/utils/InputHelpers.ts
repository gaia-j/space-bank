


export class InputHelpers{
  static nameHelper(e: Event): void {
    const input = e.target as HTMLInputElement;
    let value = input.value;
    value = value.replace(/\b\w/g, (char) => char.toUpperCase());
    value = value.replace(/[^a-zA-Z ]/g, "");
    input.value = value;
  }

  static taxIdMask(e: Event): void {
    const input = e.target as HTMLInputElement;
    let value = input.value;
    value = value.replace(/\D/g, "");
    value = value.replace(/(\d{3})(\d)/, "$1.$2");
    value = value.replace(/(\d{3})(\d)/, "$1.$2");
    value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    input.value = value;
  }

  static taxIdUnmask(value: string): string {
    return value.replace(/\D/g, "");
  }

  static birthDateHelper(e: Event): void {
    const input = e.target as HTMLInputElement;
    const date = input.valueAsDate;
    if (date && date.getFullYear() > 2024) {
      date.setFullYear(2024);
    }
    input.valueAsDate = date;
  }

  static emailHelper(e: Event): void {
    const input = e.target as HTMLInputElement;
    let value = input.value;
    value = value.replace(/[^a-zA-Z0-9.@_-]/g, "");
    value = value.replace(/\.{2,}/g, ".");
    const parts = value.split("@");
    if (parts.length > 2) {
      value = parts[0] + "@" + parts.slice(1).join("");
    }
    // value = value.replace(/^\.+|\.+$/g, "");
    input.value = value;
  }

}

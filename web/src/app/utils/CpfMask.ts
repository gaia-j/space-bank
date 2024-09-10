

export class CpfMask{
  static mask(value: string): string {
    value = value.replace(/\D/g, "");
    value = value.replace(/(\d{3})(\d)/, "$1.$2");
    value = value.replace(/(\d{3})(\d)/, "$1.$2");
    value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    return value;
  }

  static unmask(value: string): string {
    return value.replace(/\D/g, "");
  }
}

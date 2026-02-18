import fs from "fs"

export function writeInFile(filePathname: string, value: any, printLevel: number = 2): void {
    var text: string;
    if (((typeof value) === 'string') || value instanceof String) {
        text = value;
    } else {
        text = JSON.stringify(value, null, printLevel);
    }
    fs.writeFileSync(filePathname, text);
}

export function enumKeys<O extends object, K extends keyof O = keyof O>(obj: O): K[] {
    return Object.keys(obj).filter(k => Number.isNaN(+k)) as K[];
}

export function enumValues<O extends object, V>(obj: O): V[] {
    return Object.keys(obj).filter(k => Number.isNaN(+k)).map((k, _, __) => obj[k] as unknown as V);
}

export function mapValues<O extends object, V>(obj: O): V[] {
    return Object.keys(obj).map((k, _, __) => obj[k] as unknown as V);
}

export function forEach<O extends object, V>(obj: O, action: (val: V) => void): void {
    enumKeys(obj).forEach((k, _, __) => {
        action(obj[k] as unknown as V);
    });
}
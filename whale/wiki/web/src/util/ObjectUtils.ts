export class ObjectUtils {
    public static isEmpty(obj: any): boolean {
        if (typeof obj == "string") {
            return !obj || obj.replace(/\s+g/, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0)
        }
    }

    public static isNotEmpty(obj: any): boolean {
        return !this.isEmpty(obj)
    }

    public static copy(obj: object): object {
        if (this.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj))
        } else {
            return {}
        }
    }

    public static arrayToTree(array: any, parentId: number) {
        if (this.isEmpty(array)) {
            return []
        }

        const tree = []

        for (let i = 0; i < array.length; i++) {
            const item = array[i]

            if (Number(item.parent) === Number(parentId)) {
                tree.push(item)
                const children = this.arrayToTree(array, item.id)

                if (this.isNotEmpty(children)) {
                    item.children = children
                }
            }
        }

        return tree
    }
}
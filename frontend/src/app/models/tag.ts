export interface Tag {
    id: number;
    name: string;
}

export function getTagColor(tagName: string): string {
    const colors = [
        'red', 'orange', 'amber', 'yellow', 'lime',
        'green', 'emerald', 'teal', 'cyan', 'sky',
        'blue', 'indigo', 'violet', 'purple', 'fuchsia',
        'pink', 'rose'
    ]
    let colorIndex = 0;
    for (let i = 0; i < tagName.length; i++) {
        colorIndex += tagName.charCodeAt(i);
    }
    colorIndex = colorIndex % colors.length;
    return `background-color: var(--color-${colors[colorIndex]}-300)`;
}

export interface Tag {
    id: number;
    name: string;
}

/**
 * Generates a CSS style string for a tag color based on the tag name.
 * The color is determined by the sum of the character codes of the tag name,
 * which is then used to index into a predefined list of colors.
 * @param tagName - The name of the tag to generate a color for.
 * @returns A CSS style string for the background color of the tag.
 */
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

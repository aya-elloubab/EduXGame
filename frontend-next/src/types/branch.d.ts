import { Level } from './level';

export type Branch = {
    id: number;
    name: string;
    level: Level;
    subjects?: Subject[]; // Optional, in case subjects are lazy-loaded
};
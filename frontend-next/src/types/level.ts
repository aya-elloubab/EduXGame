export type Level = {
    id: number;
    name: string;
    branches: Array<{ id: number; name: string }>; // Including branches related to the level
};

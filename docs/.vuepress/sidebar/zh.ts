import {sidebar} from "vuepress-theme-hope";

export const zhSidebar = sidebar({
    "/": [
        {
            text: '介绍',
            prefix: 'introduction/',
            link: 'introduction/',
            children: 'structure'
        },
        {
            text: "指南",
            prefix: "guide/",
            children: "structure",
        }
    ],
});

import {defineUserConfig} from "vuepress";
import {shikiPlugin} from "@vuepress/plugin-shiki";
import {searchPlugin} from "@vuepress/plugin-search";
// import {googleAnalyticsPlugin} from "@vuepress/plugin-google-analytics"
import { searchConsolePlugin } from 'vuepress-plugin-china-search-console'

import theme from "./theme.js";

export default defineUserConfig({
    base: "/",

    port: 4008,

    head: [
//         ['script', {}, `
//             var _hmt = _hmt || [];
//             (function() {
//               var hm = document.createElement("script");
//               hm.src = "https://hm.baidu.com/hm.js?fab881821b3db8a7c460db1c91ea0f3a";
//               var s = document.getElementsByTagName("script")[0];
//               s.parentNode.insertBefore(hm, s);
//             })();
//         `],
        ['meta', {name: 'baidu-site-verification', content: 'codeva-DlTJUnIVzh'}],
//         ['meta', {name: 'bytedance-verification-code', content: 'fXSJ80ILSanHaf/RIZGQ'}],
    ],

    locales: {
        "/": {
            lang: "zh-CN",
            title: "EasyRelation",
            description: "EasyRelation指南",
        },
    },

    theme,

    plugins: [
        shikiPlugin({
            theme: 'one-dark-pro'
        }),
        searchPlugin({
            isSearchable: (page) => page.path !== '/'
        }),
//         googleAnalyticsPlugin({
//             id: 'G-SXEZVNR8FZ'
//         }),
        searchConsolePlugin({
            baiduId: 'eb35e18caa552284b39d427c1e06f9f7'
        })
    ]
});

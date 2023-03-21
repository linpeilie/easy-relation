import {defineUserConfig} from "vuepress";
import {shikiPlugin} from "@vuepress/plugin-shiki";
import {searchPlugin} from "@vuepress/plugin-search";
// import {googleAnalyticsPlugin} from "@vuepress/plugin-google-analytics"

import theme from "./theme.js";

export default defineUserConfig({
    base: "/",

    port: 4008,

    head: [
        ['script', {}, `
            var _hmt = _hmt || [];
            (function() {
              var hm = document.createElement("script");
              hm.src = "https://hm.baidu.com/hm.js?eb35e18caa552284b39d427c1e06f9f7";
              var s = document.getElementsByTagName("script")[0];
              s.parentNode.insertBefore(hm, s);
            })();
        `],
        ['meta', {name: 'baidu-site-verification', content: 'codeva-DlTJUnIVzh'}]
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
    ]
});

import { hopeTheme } from "vuepress-theme-hope";
import { zhNavbar } from "./navbar";
import { zhSidebar } from "./sidebar";

export default hopeTheme({
  hostname: "https://easy-relation.easii.cn",
  // 是否显示打印按钮
  print: false,

  author: {
    name: "linpeilie",
    url: "https://github.com/linpeilie",
  },

  iconAssets: "iconfont",

  logo: "/logo.svg",

  repo: "linpeilie/easy-relation",
  docsDir: 'docs',
  docsBranch: 'main',

  locales: {
    "/": {
      // navbar
      navbar: zhNavbar,

      // sidebar
      sidebar: zhSidebar,

      // footer: "默认页脚",

      displayFooter: true,

      // page meta
      metaLocales: {
        editLink: "在 GitHub 上编辑此页",
      },
    },
  },

  plugins: {
//     comment: {
//       provider: 'Giscus',
//       repo: 'linpeilie/easy-relation',
//       repoId: 'R_kgDOJA3-jg',
//       category: 'Announcements',
//       categoryId: 'DIC_kwDOJA3-js4CU8EO'
//     },
    autoCatalog: true,
    copyCode: {
      showInMobile: false
    },
    // all features are enabled for demo, only preserve features you need here
    mdEnhance: {
      align: true,
      attrs: true,
      chart: true,
      codetabs: true,
      container: true,
      demo: true,
      echarts: false,
      figure: true,
      flowchart: false,
      gfm: true,
      imgLazyload: true,
      imgMark: true,
      imgSize: true,
      include: true,
      katex: true,
      mark: true,
      mermaid: true,
      playground: {
        presets: ["ts", "vue"],
      },
      presentation: {
        plugins: ["highlight", "math", "search", "notes", "zoom"],
      },
      stylize: [
        {
          matcher: "Recommended",
          replacer: ({ tag }) => {
            if (tag === "em")
              return {
                tag: "Badge",
                attrs: { type: "tip" },
                content: "Recommended",
              };
          },
        },
      ],
      sub: true,
      sup: true,
      tabs: true,
      vPre: true,
      vuePlayground: true,
    }
  },
});

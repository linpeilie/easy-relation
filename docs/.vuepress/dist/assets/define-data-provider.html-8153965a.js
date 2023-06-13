import{_ as p,X as r,Y as c,Z as n,$ as s,a0 as e,a2 as l,a1 as o,C as t}from"./framework-6ad2459b.js";const i={},B=o(`<h2 id="定义数据提供者「dataprovider」" tabindex="-1"><a class="header-anchor" href="#定义数据提供者「dataprovider」" aria-hidden="true">#</a> 定义数据提供者「DataProvider」</h2><p>针对一个数据关联操作，DataProvider 是必须的，这是数据关联的数据提供方，在关联时，会自动调用该方法，获取并解析数据。</p><p>数据提供者所在的类要继承 <code>DataProvideService</code> 接口，同时在方法上面添加 <code>@DataProvider</code> 注解，同时在注解中指定唯一标识。</p><p>例如：</p><p>关联用户信息的数据提供者：</p><div class="language-java line-numbers-mode" data-ext="java"><pre class="shiki one-dark-pro" style="background-color:#282c34;" tabindex="0"><code><span class="line"><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Component</span></span>
<span class="line"><span style="color:#C678DD;">public</span><span style="color:#E06C75;"> </span><span style="color:#C678DD;">class</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">UserInfoDataProviderHandler</span><span style="color:#E06C75;"> </span><span style="color:#C678DD;">implements</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">DataProvideService</span><span style="color:#E06C75;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">DataProvider</span><span style="color:#E06C75;">(</span><span style="color:#E5C07B;">RelationIdentifiers</span><span style="color:#ABB2BF;">.</span><span style="color:#E5C07B;">getUserByUsername</span><span style="color:#E06C75;">)</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">public</span><span style="color:#61AFEF;"> </span><span style="color:#E5C07B;">User</span><span style="color:#61AFEF;"> getUserByUsername</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">UserQueryReq</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">req</span><span style="color:#ABB2BF;">)</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">if</span><span style="color:#ABB2BF;"> (</span><span style="color:#E5C07B;">StrUtil</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">isEmpty</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">req</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getUsername</span><span style="color:#ABB2BF;">())) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#C678DD;">throw</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">new</span><span style="color:#ABB2BF;"> </span><span style="color:#61AFEF;">IllegalArgumentException</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;username is empty&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">if</span><span style="color:#ABB2BF;"> (</span><span style="color:#98C379;">&quot;admin&quot;</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">equals</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">req</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getUsername</span><span style="color:#ABB2BF;">())) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#C678DD;">final</span><span style="color:#ABB2BF;"> </span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">new</span><span style="color:#ABB2BF;"> </span><span style="color:#61AFEF;">User</span><span style="color:#ABB2BF;">();</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">setUsername</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;admin&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">setNickName</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;管理员&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#C678DD;">return</span><span style="color:#ABB2BF;"> user;</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">return</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">null</span><span style="color:#ABB2BF;">;</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#ABB2BF;">}</span></span>
<span class="line"></span></code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="注意事项" tabindex="-1"><a class="header-anchor" href="#注意事项" aria-hidden="true">#</a> 注意事项</h2><h3 id="数据提供者唯一标识不可重复" tabindex="-1"><a class="header-anchor" href="#数据提供者唯一标识不可重复" aria-hidden="true">#</a> 数据提供者唯一标识不可重复</h3><p>数据关联时，会通过数据提供者的唯一标识来进行关联查询，所以当前唯一标识不可重复</p><h3 id="参数类型" tabindex="-1"><a class="header-anchor" href="#参数类型" aria-hidden="true">#</a> 参数类型</h3><p>数据关联查询方法入参支持的类型如下：</p><ul><li><code>String</code></li><li><code>Integer</code></li><li><code>Long</code></li><li>自定义的 Java Bean</li></ul>`,12),d=n("code",null,"String",-1),y=n("code",null,"Integer",-1),u=n("code",null,"Long",-1),F=n("h3",{id:"返回参数类型",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#返回参数类型","aria-hidden":"true"},"#"),s(" 返回参数类型")],-1),v=n("p",null,"理论情况下，支持任意的返回类型，但在使用时，需要注意关联关系。",-1),A=n("code",null,"targetField",-1),E=n("code",null,"targetField",-1),C=o(`<p>例如，上面的示例中，用户信息提供者返回的类型为 <code>User</code>，这里只需要关联其昵称，则需要在 <code>@Relation#targetField</code> 指定为 <code>nickName</code>。 如果另外提供了一个方法，只提供用户昵称信息，返回类型为 <code>String</code>，则不需要指定 <code>targetField</code>，例如如下定义：</p><div class="language-java line-numbers-mode" data-ext="java"><pre class="shiki one-dark-pro" style="background-color:#282c34;" tabindex="0"><code><span class="line"><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Component</span></span>
<span class="line"><span style="color:#C678DD;">public</span><span style="color:#E06C75;"> </span><span style="color:#C678DD;">class</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">UserInfoDataProviderHandler</span><span style="color:#E06C75;"> </span><span style="color:#C678DD;">implements</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">DataProvideService</span><span style="color:#E06C75;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">DataProvider</span><span style="color:#E06C75;">(</span><span style="color:#E5C07B;">RelationIdentifiers</span><span style="color:#ABB2BF;">.</span><span style="color:#E5C07B;">getNickNameByUsername</span><span style="color:#E06C75;">)</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">public</span><span style="color:#61AFEF;"> </span><span style="color:#E5C07B;">String</span><span style="color:#61AFEF;"> getNickNameByUsername</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">String</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">username</span><span style="color:#ABB2BF;">)</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">if</span><span style="color:#ABB2BF;"> (</span><span style="color:#98C379;">&quot;admin&quot;</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">equals</span><span style="color:#ABB2BF;">(username)) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#C678DD;">return</span><span style="color:#ABB2BF;"> </span><span style="color:#98C379;">&quot;管理员&quot;</span><span style="color:#ABB2BF;">;</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">return</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">null</span><span style="color:#ABB2BF;">;</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#ABB2BF;">}</span></span>
<span class="line"></span></code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="缓存配置" tabindex="-1"><a class="header-anchor" href="#缓存配置" aria-hidden="true">#</a> 缓存配置</h3>`,3),m=n("code",null,"@DataProvider#useCache",-1),h=n("code",null,"@Relation#cacheStrategy",-1),D=n("code",null,"CacheStrategy.DEFAULT",-1),b=o('<p>同时，还提供了缓存的失效时间配置，通过 <code>@DataProvider#cacheTimeout</code> 来指定，单位为秒(s) 。</p><h3 id="注册到-dataproviderrepository" tabindex="-1"><a class="header-anchor" href="#注册到-dataproviderrepository" aria-hidden="true">#</a> 注册到 DataProviderRepository</h3><p>当想要在框架中使用定义的数据提供者时，需要提前在 <code>DataProviderRepository</code> 中调用 <code>registerProvider</code> 方法注册该对象。</p><p>如果在 <code>SpringBoot</code> 环境下使用，则只需要声明为当前类为 Spring Bean 即可，在项目启动阶段，会自动注册。</p>',4);function _(g,f){const a=t("RouterLink");return r(),c("div",null,[B,n("p",null,[s("当参数类型为 "),d,s(" / "),y,s(" / "),u,s(" 时，配置的关联关系必须只有一个条件，且关联字段类型与参数类型相同。可以参考"),e(a,{to:"/guide/configure-relation.html#%E5%85%BC%E5%AE%B9%E5%8D%95%E4%B8%AA%E5%8F%82%E6%95%B0"},{default:l(()=>[s("配置关联关系#兼容单个参数")]),_:1})]),F,v,n("p",null,[s("当需要关联的属性，是返回类型中的某个字段，需要在关联关系中通过 "),A,s(" 属性指定当前字段名称，例如"),e(a,{to:"/guide/configure-relation.html#%E5%85%B3%E8%81%94%E7%9B%AE%E6%A0%87%E7%B1%BB%E5%9E%8B%E5%B1%9E%E6%80%A7-%E5%B9%B3%E9%93%BA%E5%85%B3%E8%81%94"},{default:l(()=>[s("配置关联关系#关联目标类型属性-平铺关联")]),_:1}),s("中的示例； 当需要关联的类型与该方法提供的类型一致，则不需要指定 "),E,s(" 字段。")]),C,n("p",null,[m,s(" 提供了在数据提供者侧配置默认缓存开关的功能。在该处配置的是针对当前数据提供者的默认缓存策略， 只有当配置关联关系时，"),h,s(" 为 "),D,s(" 时才会生效。详情可以参考"),e(a,{to:"/guide/configure-relation.html#%E5%90%AF%E7%94%A8%E7%BC%93%E5%AD%98"},{default:l(()=>[s("配置关联关系#启用缓存")]),_:1})]),b])}const U=p(i,[["render",_],["__file","define-data-provider.html.vue"]]);export{U as default};
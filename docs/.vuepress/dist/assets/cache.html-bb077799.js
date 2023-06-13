import{_ as o,X as i,Y as c,Z as a,$ as s,a0 as e,a2 as l,a1 as t,C as p}from"./framework-6ad2459b.js";const r={},d=t('<h2 id="缓存分层" tabindex="-1"><a class="header-anchor" href="#缓存分层" aria-hidden="true">#</a> 缓存分层</h2><p>EasyRelation 中共设计有两级缓存，参考了 Mybatis 中的设计，在进行数据关联时，会依次经过 <code>一级缓存 --&gt; 二级缓存 ---&gt; 数据提供源</code>，从而提高数据获取的效率。</p><p>这里简单了解一下一级缓存与二级缓存：</p><ul><li><strong>一级缓存</strong>：单次数据关联操作内的缓存，缓存的数据只在这个关联过程内有效，一级缓存根据一定规则，会自动开启。</li></ul><blockquote><p>这里的单次数据关联操作指的是调用一次 <code>injectRelation</code> 方法内的执行流程。</p></blockquote><ul><li><strong>二级缓存</strong>：全局缓存，比如使用 Redis 作为缓存，二级缓存需要手动开启。</li></ul><h2 id="一级缓存" tabindex="-1"><a class="header-anchor" href="#一级缓存" aria-hidden="true">#</a> 一级缓存</h2><h3 id="为什么需要一级缓存" tabindex="-1"><a class="header-anchor" href="#为什么需要一级缓存" aria-hidden="true">#</a> 为什么需要一级缓存</h3>',8),y=t('<p>为了解决这个问题，EasyRelation 会在执行时，进行判断是否需要一级缓存，当需要时，会自动打开缓存，缓存条件和相应的结果，从而保证性能和同一次关联操作中的一致性问题。</p><h3 id="一级缓存实现" tabindex="-1"><a class="header-anchor" href="#一级缓存实现" aria-hidden="true">#</a> 一级缓存实现</h3><p>当一级缓存开启状态时，会先到缓存中查询是否有，如果没有，则经过二级缓存、数据提供者，获取数据，最终存储到一级缓存中。</p><p>一级缓存是在当前线程的 <code>ThreadLocal</code> 中绑定了一个 Map 对象，执行前根据一定规则开启，执行后，会自动清除。</p><h3 id="一级缓存的开启条件" tabindex="-1"><a class="header-anchor" href="#一级缓存的开启条件" aria-hidden="true">#</a> 一级缓存的开启条件</h3><p>为了进一步节省性能，一级缓存并不会在每一次关联操作中开启，只会在判断需要的时候才会开启。</p><p>目前当下面两种情况时，会自动开启：</p>',7),h=a("li",null,"当一次对一个集合中的元素进行关联时；",-1),E=t(`<p>即，EasyRelation 会判断不会对同一个数据提供源进行多次调用时，则不会开启一级缓存。</p><h2 id="二级缓存" tabindex="-1"><a class="header-anchor" href="#二级缓存" aria-hidden="true">#</a> 二级缓存</h2><p>二级缓存主要是为了全局缓存，当没有命中一级缓存时，首先会到二级缓存中查找，如果找到相应的值则直接解析返回。</p><h3 id="二级缓存的实现" tabindex="-1"><a class="header-anchor" href="#二级缓存的实现" aria-hidden="true">#</a> 二级缓存的实现</h3><p>框架中提供了一个二级缓存的实现，实现比较简单，因为一般项目中也会用 Redis 来实现缓存。</p><p>针对二级缓存定义了一个接口 —— <code>RelationCache</code>，接口定义如下：</p><div class="language-java line-numbers-mode" data-ext="java"><pre class="shiki one-dark-pro" style="background-color:#282c34;" tabindex="0"><code><span class="line"><span style="color:#C678DD;">public</span><span style="color:#E06C75;"> </span><span style="color:#C678DD;">interface</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">RelationCache</span><span style="color:#E06C75;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">    /**</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * 指定键值是否存在</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     *</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@param</span><span style="color:#7F848E;font-style:italic;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#7F848E;font-style:italic;"> 键</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@return</span><span style="color:#7F848E;font-style:italic;"> 指定键值是否存在</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     */</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">boolean</span><span style="color:#61AFEF;"> hasKey</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">String</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">    /**</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * 保存缓存</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     *</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@param</span><span style="color:#7F848E;font-style:italic;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#7F848E;font-style:italic;">   键值</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@param</span><span style="color:#7F848E;font-style:italic;"> </span><span style="color:#E06C75;font-style:italic;">value</span><span style="color:#7F848E;font-style:italic;"> 实际值</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@param</span><span style="color:#7F848E;font-style:italic;"> </span><span style="color:#E06C75;font-style:italic;">time</span><span style="color:#7F848E;font-style:italic;">  过期时间，单位：ms</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     */</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">void</span><span style="color:#61AFEF;"> set</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">String</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#ABB2BF;">, </span><span style="color:#E5C07B;">Object</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">value</span><span style="color:#ABB2BF;">, </span><span style="color:#C678DD;">long</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">time</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">    /**</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * 获取缓存数据</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     *</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@param</span><span style="color:#7F848E;font-style:italic;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#7F848E;font-style:italic;"> 键值</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     * </span><span style="color:#C678DD;font-style:italic;">@return</span><span style="color:#7F848E;font-style:italic;"> {@link Object} key对应的值</span></span>
<span class="line"><span style="color:#7F848E;font-style:italic;">     */</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#E5C07B;">Object</span><span style="color:#61AFEF;"> get</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">String</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;font-style:italic;">key</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"></span>
<span class="line"><span style="color:#ABB2BF;">}</span></span>
<span class="line"></span></code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h4 id="默认实现" tabindex="-1"><a class="header-anchor" href="#默认实现" aria-hidden="true">#</a> 默认实现</h4><p>框架中的默认实现，是基于两个 <code>Map</code> 和 <code>Tasker</code> 来实现的。</p><p>保存一个 KV 时，会同时保存其过期时间，通过 <code>Tasker</code> 来过期 key。</p><h4 id="集成-redis-作为-key" tabindex="-1"><a class="header-anchor" href="#集成-redis-作为-key" aria-hidden="true">#</a> 集成 Redis 作为 Key</h4><p>EasyRelation 可以提供了 Redis 缓存的插件包，其中定义了 JDK、Jackson 两种 value 序列化方式。</p><p>具体使用可以参考<a href="/plugins/redis-cache">Redis 缓存</a></p><h4 id="自定义缓存" tabindex="-1"><a class="header-anchor" href="#自定义缓存" aria-hidden="true">#</a> 自定义缓存</h4><p>当要实现一个二级缓存时，需要实现该接口，并且声明 <code>InjectRelation</code> 对象时，传入该实现类。</p><p>如果使用 SpringBoot，将自定义缓存的实现类声明为一个 Spring 的 Bean 即可。</p><h3 id="二级缓存的开启" tabindex="-1"><a class="header-anchor" href="#二级缓存的开启" aria-hidden="true">#</a> 二级缓存的开启</h3><p>二级缓存<strong>默认是禁用状态，需要手动开启</strong>。</p>`,18),u=a("code",null,"DataProvider",-1),B=a("code",null,"useCache",-1),f=a("code",null,"@Relation",-1),v=a("code",null,"cacheStrategy",-1),F=a("code",null,"DEFAULT",-1),m=a("p",null,[s("同样还支持每个关联类型内部的个性化配置，比如通过 "),a("code",null,"@Relation"),s(" 中的 "),a("code",null,"cacheStrategy"),s(" 配置强制开启缓存或强制关闭缓存。")],-1),b=a("code",null,"DataProvider",-1),C=t('<h2 id="缓存-key-的生成规则" tabindex="-1"><a class="header-anchor" href="#缓存-key-的生成规则" aria-hidden="true">#</a> 缓存 Key 的生成规则</h2><p><code>数据提供者唯一标识</code> + <code>-</code> + 参数列表（<code>参数名称</code> + <code>-</code> + <code>参数值</code>）。</p><p>例如：</p><p><code>getUserByUsername-username-admin</code></p>',4);function _(A,g){const n=p("RouterLink");return i(),c("div",null,[d,a("p",null,[s("在进行数据关联时，在对一个集合进行关联，或者一个对象内有"),e(n,{to:"/guide/configure-relation.html#%E5%85%B3%E8%81%94%E7%9B%AE%E6%A0%87%E7%B1%BB%E5%9E%8B%E5%B1%9E%E6%80%A7-%E5%B9%B3%E9%93%BA%E5%85%B3%E8%81%94"},{default:l(()=>[s("平铺关联")]),_:1}),s("的操作时， 针对同一个数据提供者，且条件相同时，每一次关联都查询一次数据库，或者调用一次 Rpc 接口，会白白浪费系统的资源，并且还会导致类似于数据库中的脏读、幻读、不可重复读等问题。")]),y,a("ol",null,[h,a("li",null,[s("当在一个对象中出现"),e(n,{to:"/guide/configure-relation.html#%E5%85%B3%E8%81%94%E7%9B%AE%E6%A0%87%E7%B1%BB%E5%9E%8B%E5%B1%9E%E6%80%A7-%E5%B9%B3%E9%93%BA%E5%85%B3%E8%81%94"},{default:l(()=>[s("平铺关联")]),_:1}),s("的操作时；")])]),E,a("p",null,[s("首先，在配置 "),u,s(" 时，可以通过 "),B,s(" 配置当前数据提供者的默认缓存开关配置，当前配置只有在配置 "),f,s(" 时， "),v,s(" 为 "),F,s(" 时才会生效，详情可以参考"),e(n,{to:"/guide/configure-relation.html#%E5%90%AF%E7%94%A8%E7%BC%93%E5%AD%98"},{default:l(()=>[s("配置关联关系#启用缓存")]),_:1})]),m,a("p",null,[s("当通过该属性配置强制开启/关闭时，会忽略 "),b,s(" 中的缓存开关配置。详情可以参考"),e(n,{to:"/guide/configure-relation.html#%E5%90%AF%E7%94%A8%E7%BC%93%E5%AD%98"},{default:l(()=>[s("配置关联关系#启用缓存")]),_:1})]),C])}const k=o(r,[["render",_],["__file","cache.html.vue"]]);export{k as default};

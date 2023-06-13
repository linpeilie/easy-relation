import{_ as p,X as o,Y as e,Z as n,$ as s,a0 as B,a2 as c,a1 as l,C as t}from"./framework-6ad2459b.js";const r={},i=l('<h2 id="injectrelation" tabindex="-1"><a class="header-anchor" href="#injectrelation" aria-hidden="true">#</a> InjectRelation</h2><p>当配置好关联关系和数据提供者时，就可以通过 <code>InjectRelation</code> 提供的方法，在需要的时候进行数据关联了。</p><p>当需要数据关联时，需要先获取 <code>InjectRelation</code> 实例（例如，可以用 Spring 注入的方式），再调用相应的方法来进行数据关联。</p><p>其内部提供了两个方法：</p><ul><li><code>public &lt;T&gt; void injectRelation(T t, String... relationFields)</code></li><li><code>public &lt;T&gt; void injectRelation(List&lt;T&gt; list, String... relationFields)</code></li></ul><p>这两个方法分别针对单个对象和集合来进行关联。</p>',6),y=l(`<p>第二个参数是指定要关联的属性名称，当不指定时，默认关联配置的所有需要关联的属性。</p><p>示例：</p><div class="language-java line-numbers-mode" data-ext="java"><pre class="shiki one-dark-pro" style="background-color:#282c34;" tabindex="0"><code><span class="line"><span style="color:#C678DD;">class</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">InjectRelationTest</span><span style="color:#E06C75;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Autowired</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">private</span><span style="color:#E06C75;"> </span><span style="color:#E5C07B;">InjectRelation</span><span style="color:#E06C75;"> injectRelation</span><span style="color:#ABB2BF;">;</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">private</span><span style="color:#61AFEF;"> </span><span style="color:#E5C07B;">User</span><span style="color:#61AFEF;"> initUser</span><span style="color:#ABB2BF;">()</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">new</span><span style="color:#ABB2BF;"> </span><span style="color:#61AFEF;">User</span><span style="color:#ABB2BF;">();</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">setUsername</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;admin&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">setNickName</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;管理员&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">setCreateUsername</span><span style="color:#ABB2BF;">(</span><span style="color:#98C379;">&quot;admin&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">return</span><span style="color:#ABB2BF;"> user;</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Test</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">void</span><span style="color:#61AFEF;"> injectRelation</span><span style="color:#ABB2BF;">()</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">final</span><span style="color:#ABB2BF;"> </span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#61AFEF;">initUser</span><span style="color:#ABB2BF;">();</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">injectRelation</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">injectRelation</span><span style="color:#ABB2BF;">(user);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">System</span><span style="color:#ABB2BF;">.</span><span style="color:#E5C07B;">out</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">println</span><span style="color:#ABB2BF;">(user);</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Test</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">public</span><span style="color:#61AFEF;"> </span><span style="color:#C678DD;">void</span><span style="color:#61AFEF;"> injectSingleField</span><span style="color:#ABB2BF;">()</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">final</span><span style="color:#ABB2BF;"> </span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#61AFEF;">initUser</span><span style="color:#ABB2BF;">();</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">injectRelation</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">injectRelation</span><span style="color:#ABB2BF;">(user, </span><span style="color:#98C379;">&quot;createNickName&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">System</span><span style="color:#ABB2BF;">.</span><span style="color:#E5C07B;">out</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">println</span><span style="color:#ABB2BF;">(user);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">isNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getRole</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">isNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getPermissions</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Test</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">public</span><span style="color:#61AFEF;"> </span><span style="color:#C678DD;">void</span><span style="color:#61AFEF;"> injectList</span><span style="color:#ABB2BF;">()</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">List</span><span style="color:#ABB2BF;">&lt;</span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;">&gt; </span><span style="color:#E06C75;">userList</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">new</span><span style="color:#ABB2BF;"> </span><span style="color:#E5C07B;">ArrayList</span><span style="color:#ABB2BF;">&lt;&gt;();</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">for</span><span style="color:#ABB2BF;"> (</span><span style="color:#C678DD;">int</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">i</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">0</span><span style="color:#ABB2BF;">; i </span><span style="color:#56B6C2;">&lt;</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">10</span><span style="color:#ABB2BF;">; i++) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">userList</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">add</span><span style="color:#ABB2BF;">(</span><span style="color:#61AFEF;">initUser</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">injectRelation</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">injectRelation</span><span style="color:#ABB2BF;">(userList);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">for</span><span style="color:#ABB2BF;"> (</span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">:</span><span style="color:#ABB2BF;"> userList) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">notNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getRole</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">notNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getPermissions</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">notNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getCreateNickName</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#ABB2BF;">@</span><span style="color:#E5C07B;">Test</span></span>
<span class="line"><span style="color:#E06C75;">    </span><span style="color:#C678DD;">public</span><span style="color:#61AFEF;"> </span><span style="color:#C678DD;">void</span><span style="color:#61AFEF;"> injectListSingleField</span><span style="color:#ABB2BF;">()</span><span style="color:#61AFEF;"> </span><span style="color:#ABB2BF;">{</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">List</span><span style="color:#ABB2BF;">&lt;</span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;">&gt; </span><span style="color:#E06C75;">userList</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">new</span><span style="color:#ABB2BF;"> </span><span style="color:#E5C07B;">ArrayList</span><span style="color:#ABB2BF;">&lt;&gt;();</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">for</span><span style="color:#ABB2BF;"> (</span><span style="color:#C678DD;">int</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">i</span><span style="color:#ABB2BF;"> </span><span style="color:#56B6C2;">=</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">0</span><span style="color:#ABB2BF;">; i </span><span style="color:#56B6C2;">&lt;</span><span style="color:#ABB2BF;"> </span><span style="color:#D19A66;">10</span><span style="color:#ABB2BF;">; i++) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">userList</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">add</span><span style="color:#ABB2BF;">(</span><span style="color:#61AFEF;">initUser</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#E5C07B;">injectRelation</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">injectRelation</span><span style="color:#ABB2BF;">(userList, </span><span style="color:#98C379;">&quot;createNickName&quot;</span><span style="color:#ABB2BF;">);</span></span>
<span class="line"><span style="color:#ABB2BF;">        </span><span style="color:#C678DD;">for</span><span style="color:#ABB2BF;"> (</span><span style="color:#E5C07B;">User</span><span style="color:#ABB2BF;"> </span><span style="color:#E06C75;">user</span><span style="color:#ABB2BF;"> </span><span style="color:#C678DD;">:</span><span style="color:#ABB2BF;"> userList) {</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">isNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getRole</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">isNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getPermissions</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">            </span><span style="color:#E5C07B;">Assert</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">notNull</span><span style="color:#ABB2BF;">(</span><span style="color:#E5C07B;">user</span><span style="color:#ABB2BF;">.</span><span style="color:#61AFEF;">getCreateNickName</span><span style="color:#ABB2BF;">());</span></span>
<span class="line"><span style="color:#ABB2BF;">        }</span></span>
<span class="line"><span style="color:#ABB2BF;">    }</span></span>
<span class="line"></span>
<span class="line"><span style="color:#ABB2BF;">}</span></span>
<span class="line"></span></code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,3);function F(A,d){const a=t("RouterLink");return o(),e("div",null,[i,n("blockquote",null,[n("p",null,[s("这里需要注意，在处理集合时，会默认开启一级缓存（参考"),B(a,{to:"/extension/cache.html"},{default:c(()=>[s("缓存")]),_:1}),s("），所以当集合时，建议调用第二个方法。")])]),y])}const v=p(r,[["render",F],["__file","inject-relation.html.vue"]]);export{v as default};

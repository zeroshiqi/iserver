<script>
/** 
 * 在页面中任何嵌套层次的窗口中获取顶层窗口 
 * @return 当前页面的顶层窗口对象 
 */
function getTopWinow(){  
   var p = window;  
   while(p != p.parent){  
       p = p.parent;  
   }  
   return p;  
}
//调用：
var top = getTopWinow(); //获取当前页面的顶层窗口对象
top.location.href = '${app}/admin/forward'; //跳转到登陆页面
</script>
/**
 * 
 */
function beforeSubmitWithPassWordCheck(form,isPublish)
{disableSubmit();try{tinyMCE.get("addBlog_blogContent").save({format:"raw"})}catch(e){}if(isBlank(form.title.value)||"杈撳叆鏃ュ織鏍囬"==form.title.value){var dt=new Date,msg="鎮ㄥ皻鏈～鍐欐棩蹇楁爣棰橈紝纭浣跨敤鏃ユ湡浣滀负榛樿鏍囬鍚楋紵";return require(["ui/dialog"],function(){var e=jQuery('<div style="line-height: 40px;font-size: 14px;font-family:"Microsoft YaHei";">'+msg+"</div>");e.dialog({title:" 鎻愰啋",buttons:{"纭畾":function(e){form.title.value=dt.getFullYear()+"-"+(dt.getMonth()+1)+"-"+dt.getDate(),saveWithPassWordCheck(isPublish),$(this).dialog("destroy")},"鍙栨秷":function(){$(this).dialog("destroy")}}})}),enableSubmit(),!1}if(isBlank(form.body.value.replace(/\&nbsp\;/gi,"").replace(/(?:<br[^>]*>|<p>|<\/p>)/gi,""))){var alertDom=jQuery('<div style="line-height: 40px;font-size: 14px;font-family:"Microsoft YaHei";">鏈～鍐欐棩蹇楀唴瀹�</div>');return alertDom.dialog({title:"鎻愰啋"}),enableSubmit(),!1}return getBlogMedia(),isPublish&&Number(eval("("+jQuery("#hdLoginDays").attr("data-tips")+")").range)<=5?(require(["ui/dialog"],function(){$.dialog.verify("璇风‘璁ゆ偍涓嶆槸鏈哄櫒","albumInfo").option({confirm:function(e,t){var i=$("#addBlog_editorForm");i.find("#rKey").remove(),i.append('<input type="hidden" name="rKey" id="rKey" value="'+t+'"/>'),"blog"==i.attr("isedit")&&i.append('<input type="hidden" name="blogId" id="blogId" value="'+i.attr("blogId")+'"/>'),$.ajax({url:i.attr("blog"==i.attr("isedit")?"editaction":"action"),type:i.attr("method"),data:i.serialize(),dataType:"json",success:function(e){0===e.code?window.location=e.url:($("#addBlog_publishBtn").val("鍙戝竷鏃ュ織"),$.dialog.error(e.msg))}})}})}),!1):!0}
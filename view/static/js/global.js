/* 保存数据库配置 */
function saveSettings(data) {
  window.webEngine.saveSettings(data);
}

/* 保存数据库配置回调 */
function saveSettingsCallback(data) {
  $(".btn-default").click();
}

/* 加载配置 */
function loadSettings(id) {
  window.webEngine.loadSettings(id);
}

function loadSettingDetail(id) {
  window.webEngine.loadSettingDetail(id);
}

function loadSettingDetailCallback(data) {
  setViewSettings(JSON.parse(data));
  $(".btn-default").click();
}



/* 加载配置回调 */
function loadSettingsCallback(data) {
  console.log(data)
  var json = JSON.parse(data);

  var html = "";
  $.each(json,function () {
    var name = this.name;
    var id = this.id;
    console.log("name="+name+",id=" + id);
    html += "<option value='"+ id+"'>"+name+"</option>";
    console.log(html)
  });

  if (html){
    $("#choose-settings-select").empty().append(html);
  }
  $("#choose-settings-modal").modal('show');

}


/* 加载数据库连接 */
function loadLinks() {
  try{
    window.webEngine.loadLinks();

  }catch (e) {
    $(".li-level-1").parent().removeClass("hidden");
    console.log(e);
  }
}

/* 加载数据库连接回调 */
function loadLinksCallback(data) {
  $("#sidebar").append(data);
}

/* 更新数据库连接 */
function updateLink(data) {
  window.webEngine.updateLink(data);
}

/* 更新数据库连接回调 */
function updateLinkCallback(data) {
  var obj = JSON.parse(data);
  $(".a-level-1[link-id='"+obj.id+"']").find("span").text(obj.name);
  $(".btn-default").click();
}

/* 更新数据库连接 */
function saveLink(data) {
  window.webEngine.saveLink(data);
}

/* 保存数据库连接 */
function saveLinkCallback(data) {
  $("#sidebar").append(data);
  $(".btn-default").click();
}

/* 删除数据库连接 */
function deleteLink(id) {
  window.webEngine.deleteLink(id);
}

/* 删除数据库连接回调 */
function deleteLinkCallback(id) {
  $(".a-level-1[link-id='"+id+"']").parent().parent().remove();
  $(".btn-default").click();
}

/* 加载当前连接下的数据库 */
function loadDatabases(id) {
  window.webEngine.loadDatabases(id);
}

/* 加载当前连接下的数据库回调 */
function loadDatabasesCallback(id,data) {
  $(".a-level-1[link-id='"+id+"']").siblings().remove();
  $(".a-level-1[link-id='"+id+"']").parent().append(data);
  $(".btn-default").click();
}

/* 生成代码 */
function createCode(data) {
  window.webEngine.createCode(data);
}

/* 生成代码回调 */
function createCodeCallback() {
  $("#tip-modal").modal("show");
}

function loadDefaultMappings(id) {
  window.webEngine.loadDefaultMappings(id);
}

function loadDefaultMappingsCallback(data) {
  $(".mapping-content").empty().append(data);
}

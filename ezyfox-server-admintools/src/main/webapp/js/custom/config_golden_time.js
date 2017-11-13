toastr.options = {
        closeButton: false,
        progressBar: false,
        showMethod: 'slideDown',
        timeOut: 1500
  };
window.onload = function(){
	var accessToken = document.getElementById('accessToken').value;
	$.ajax({
		type : "GET", 
		url : "api/get_x2_golden?accessToken=" + accessToken,
		dataType : "json",
		async: true,
		success : function(result) {
			showJqGrid(result,accessToken);
		}
	});
	
}
function startX2(){
	var accessToken = document.getElementById('accessToken').value;
	$.ajax({
		type : "GET", 
		url : "api/start_x2_golden?accessToken=" + accessToken+"&write=true",
		dataType : "json",
		async: true,
		success : function(result) {
			var callback = result['callback'];
			if(callback == "NO_DATA"){
				toastr.error("Không có dữ liệu")
			} else {
				$('#table_list_2').trigger('reloadGrid');
				toastr.success("Khởi chạy giờ vàng thành công");
			}
		
		},
		error:function(err){
			toastr.error("Bạn không có quyền truy cập chức năng này");
		}
	});
}
function stopX2(){
	var accessToken = document.getElementById('accessToken').value;
	$.ajax({
		type : "GET", 
		url : "api/stop_x2_golden?accessToken=" + accessToken+"&write=true",
		dataType : "json",
		async: true,
		success : function(result) {
			var callback = result['callback'];
			if(callback == "NO_DATA"){
				toastr.error("Không có dữ liệu")
			} else {
				$('#table_list_2').trigger('reloadGrid');
				toastr.success("Dừng giờ vàng thành công");
			}
		},
		error:function(err){
			toastr.error("Bạn không có quyền truy cập chức năng này");
		}
	});
}
function isGoldTime(x2,
		x2SStart,x2SEnd,x2SBetId,x2SRatio){
	var result = false;
	for(var k in x2){
		var x2BetId = x2[k].bettingTypeId;
		var x2Ratio = x2[k].bettingTypeId;
		var x2Start = x2[k].startHour + ":"+x2[k].startMinute;
		var x2End = x2[k].endHour + ":"+x2[k].endMinute;
		if(x2SBetId == x2BetId && x2SRatio == x2Ratio
				&& x2SStart == x2Start && x2SEnd == x2End){
			result = true;
		}
	}
	return result;
	
}
function showJqGrid(result,accessToken){
	var respone = result['list'];
	var banks = result['banks'];
	var x2 = result['x2'];
	console.log("X2:"+JSON.stringify(x2));
	var data = [];
	var betting = [];
	var roomSelect = [];
	var url;
		for(var key in respone){
			var betID = respone[key].bettingTypeId;
			var ratio = respone[key].multipler;
			var time = respone[key].creationDate;
			var state = respone[key].enable;
			var start =  respone[key].startHour +":" +respone[key].startMinute;
			var end =  respone[key].endHour +":" +respone[key].endMinute;
			var gold = respone[key].gold;
			console.log("Coin:"+banks[betID].coin);
			data.push({stt: key, ratio: ratio, room: banks[betID].coin, start: start,end:end, state: respone[key].enable,gold:gold});
		}
		console.log("DAAT:"+JSON.stringify(data));
		for(var key in banks){
			betting.push({betId:key,room:banks[key].coin})
		}
		for(var key in banks){
			roomSelect.push(banks[key].coin);
		}
		
	$("#table_list_2").jqGrid({
        data: data,
        datatype: "local",
        height: 500,
        autowidth: true,
        shrinkToFit: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames:['STT','Mức cược','Hệ số nhân','Mức cược', 'Bắt đầu','Kết thúc','Trạng thái',"Giờ vàng"],
        colModel:[
            {name:'stt',index:'stt', editable: false, width:60, sorttype:"int",formater:'int',search:true},
            { name: 'room', index: 'room', editable: false, width: 100 },
            {name:'ratio',index:'ratio', editable: true, width:90},
            {name:'room',index:'room',jsonmap:'room', editable: true, width:100, align:"left",edittype: "select", 
            	formatter: 'select',editrules: { edithidden: true },hidden: true, 
            	editoptions:{
            		dataEvents:[{
            			type: 'change',
                        fn: function (e) {
                            var type = $(e.target).val();
                            return type;
                        }
            		}],
            		value:roomSelect
            }},
            {name: 'start', jsonmap: "start", index: 'start', editable: true, width: 85, align: "right", sorttype: "string", formatter: "string",
                formatoptions: { srcformat: "ISO8601Long", newformat: "H:i A" },
                editoptions: {
                    dataInit: function (el) {
                        setTimeout(function () {
                            $(el).clockpicker({ placement: 'top', align: 'left', donetext: 'Done' });
                        }, 200);
                    }
                }
            }, 
            {name: 'end', jsonmap: "end", index: 'end', editable: true, width: 85, align: "right", sorttype: "string", formatter: "string",
                formatoptions: { srcformat: "ISO8601Long", newformat: "H:i A" },
                editoptions: {
                    dataInit: function (el) {
                        setTimeout(function () {
                            $(el).clockpicker({ placement: 'top', align: 'left', donetext: 'Done' });
                        }, 200);
                    }
                }
            },
            {
                name: 'state', index: 'state', editable: true, width: 100, edittype: 'checkbox', 
                editoptions: { value: "true:false" },align:"center", formatter: "checkbox"},
                { name: 'gold', index: 'gold', editable: false, width: 100,align:"center"},
        ],
        pager: "#pager_list_2",
        viewrecords: true,
        caption: "Giờ vàng",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        hidegrid: false
    });

     // Setup buttons
	 $("#table_list_2").jqGrid('navGrid', '#pager_list_2',
                { edit: true, add: true, del: true, search: false },
                {
                    height: 'auto',
                    reloadAfterSubmit: true,
                    afterComplete: function (response, data, postd) {
                    	if(response.responseText.includes("<!DOCTYPE html>")){
                    		toastr.error("Bạn không có quyền truy cập chức năng này");
                    	} else {
                    		var callback = JSON.parse(response.responseText)['callBack'];
                        	if(callback == -1){
                        		toastr.error("Thời gian không hợp lệ");
                        	} else if(callback == 1){
                        		$('#table_list_2').trigger('reloadGrid');
                        		toastr.success("Cập nhật thành công");
                        	}
                    	}
                    	
                    },
                    closeAfterEdit: true,
                    beforeSubmit: function (postdata, formid) {
                    	console.log("POST DATA:"+JSON.stringify(postdata));
                        return [true, {oldData:postdata}];
                    },
                    onInitializeForm: function (formid) {
                    },
                    onclickSubmit: function (options, postdata) {
                        var myGrid = $('#table_list_2'),
                            selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                            cellRatio = myGrid.jqGrid('getCell', selRowId, 'ratio'),
                            cellRoom = myGrid.jqGrid('getCell', selRowId, 'room'),
                            cellStart = myGrid.jqGrid('getCell', selRowId, 'start'),
                            cellEnd = myGrid.jqGrid('getCell', selRowId, 'end'),
                            cellState = myGrid.jqGrid('getCell', selRowId, 'state');
                        return { oldratio:cellRatio, oldroom:cellRoom, oldstart:cellStart, 
                        	oldend:cellEnd,oldstate:cellState  };
                    },
                    mtype: "POST",
                    closeAfterEdit: true,
                    url:"api/update_x2_golden?accessToken=" + accessToken+"&write=true"
                },{
                	height: 'auto',
                    reloadAfterSubmit: true,
                    afterComplete: function (response, data, postd) {
                    	if(response.responseText.includes("<!DOCTYPE html>")){
                    		toastr.error("Bạn không có quyền truy cập chức năng này");
                    	} else {
                    		var callback = JSON.parse(response.responseText)['callBack'];
                        	if(callback == -1){
                        		toastr.error("Thời gian không hợp lệ");
                        	} else if(callback == 1){
                        		$('#table_list_2').trigger('reloadGrid');
                        		toastr.success("Thêm mới thành công");
                        	}
                    	}
                    	
                    },
                    closeAfterEdit: true,
                    beforeSubmit: function (postdata, formid) {
                    	
                        return [true, ""];
                    },
                    onInitializeForm: function (formid) {
                    },
                    closeAfterAdd: true,
                    mtype: "POST",
                    url:"api/add_x2_golden?accessToken=" + accessToken+"&write=true"
                },
                {
                	height: 'auto',
                    afterComplete: function (response, data, postd) {
                    	if(response.responseText.includes("<!DOCTYPE html>")){
                    		toastr.error("Bạn không có quyền truy cập chức năng này");
                    	} else {
                    		$('#table_list_2').trigger('reloadGrid');	
                        	toastr.error("Xóa giờ vàng thành công");
                    	}
                    },
                    onclickSubmit: function (options, postdata) {
                        var myGrid = $('#table_list_2'),
                            selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                            cellRoom = myGrid.jqGrid('getCell', selRowId, 'room'),
                            cellStart = myGrid.jqGrid('getCell', selRowId, 'start'),
                            cellEnd = myGrid.jqGrid('getCell', selRowId, 'end');
                        return {  room:cellRoom, start:cellStart, end:cellEnd };
                    },
                    closeAfterEdit: true,
                    beforeSubmit: function (postdata, formid) {
                    	
                        return [true, ""];
                    },
                    onInitializeForm: function (formid) {
                    	
                    },
                    mtype: "POST",
                    url:"api/remove_x2_golden?accessToken=" + accessToken+"&write=true"
                }
            );

     // Add responsive to jqGrid
     $(window).bind('resize', function () {
         var width = $('.jqGrid_wrapper').width();
         $('#table_list_2').setGridWidth(width);
     });
     $(window).bind('resize', function () {
            var width = $('.jqGrid_wrapper').width();
            $('#table_list_2').setGridWidth(width);
        });
}
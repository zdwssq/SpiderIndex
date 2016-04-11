/**
 * 
 */
var keywords = null;
var currentNum = null;
var pageSize = 10;
var qr = location.search;

$(function() {
	$.ajax({
		url : "searchRes" + qr,
		type : "GET",
		async : false,
		dataType : "json",
		success : function(data) {
			showResultList(data);
			showPagination(data);
		},
		error : function(data) {
			alert("请求失败 " + data);
		}
	});
})

function showResultList(data) {
	keywords = data.keywords;
	$("#keywords").val(keywords);

	var searchRes = data.pageDatas;
	if (searchRes.length == 0) {
		$("#lists").html("暂无数据");
	} else {
		var items = "";
		$.each(searchRes, function(key, value) {
			items = items + "<div class='item'>"
					+ "<div class='itemTitle'><a href='" + value.baseUrl + "'>"
					+ value.title + "</a></div>" + "<div class='itemContent'>"
					+ value.text + "</div>" + "</div>";

		});
		$("#lists").html(items);
	}
}

function showPagination(data) {
	currentNum = data.pageNo;
	var pageCount = data.pageCount;
	var items = null;
	var last = currentNum - 1;
	var next = currentNum + 1;

	if (currentNum == 1) {
		items = "<ul class='pagination'><li class='disabled'><a href='#' onclick='selectByNum(this);'>&laquo;</a></li>";
	} else {
		items = "<ul class='pagination'><li><a href='#' onclick='selectByNum(this);'>&laquo;</a></li>";
	}

	for (var i = 0; i < pageCount; i++) {
		var index = i + 1;
		if (i + 1 != currentNum) {
			items = items + "<li><a href='#' onclick='selectByNum(this);'>"
					+ index + "</a></li>";
		} else {
			items = items + "<li class='disabled'><a href='#'>" + index
					+ "</a></li>";
		}
	}

	if (currentNum == pageCount) {
		items = items
				+ "<li class='disabled'><a href='#'>&raquo;</a></li></ul>";
	} else {
		items = items
				+ "<li><a href='#' onclick='selectByNum(this);'>&raquo;</a></li></ul>";
	}

	$("#pages").html(items);
}

function selectByNum(obj) {
	var ele = obj.innerHTML;
	var newNum = null;
	if (ele == "«") {
		newNum = currentNum - 1;
	} else if (ele == "»") {
		newNum = currentNum + 1;
	} else {
		newNum = ele;
	}

	//qr = qr.replace(currentNum, newNum);
	qr = qr.split("&")[0]+"&currentNum=" + newNum + "&pageSize=" + pageSize;
	$.ajax({
		url : "searchRes" + qr,
		type : "GET",
		async : false,
		dataType : "json",
		success : function(data) {
			showResultList(data);
			showPagination(data);
		},
		error : function(data) {
			alert("请求失败 " + data);
		}
	});
}

'use strict'

//非同期初期画面ーーーーーーーーーーーーーーー
document.addEventListener("DOMContentLoaded", function() {
	if (window.location.pathname.endsWith("TodoServlet")) {
		colorDates();
	}
	if (window.location.pathname.endsWith("index2.jsp")) {
		newList();
		defaultSort();
	}
});
//ナビ項目の下に印ーーーーーーーーーーーーーーー
const naviList = document.querySelectorAll('ul li');
naviList.forEach((li, index) => {// クリック位置を保存
	li.addEventListener('click', () => {
		sessionStorage.setItem('clickNavi', index);
	});
});
document.addEventListener('DOMContentLoaded', () => {// リロード後にクリック位置取得
	const sessionNavi = sessionStorage.getItem('clickNavi');
	if (sessionNavi === null) return;
	naviList.forEach(li => li.classList.remove('show'));
	naviList[sessionNavi].classList.add('show');
});
//初期画面の並びーーーーーーーーーーーーーー
function defaultSort() {
	const sessionData = document.getElementById("sessionData");
	const sort  = sessionData.dataset.sort || 'id';
	const order = sessionData.dataset.order || 'DESC';
	
	const theadTh = document.querySelectorAll("thead th");	
	theadTh.forEach(th => {
		if(th.dataset.sort === sort) {
			th.classList.add(order === 'DESC' ? 'showDown' : 'showUp')
		}
	});	
}
//３日前、赤色ーーーーーーーーーーーーーーー
function colorDates() {	
	const tds = document.querySelectorAll("tbody .date");
	
	let now = new Date();
	now.setHours(0, 0, 0, 0);
	
	let alertDay = new Date(now);
	alertDay.setDate(now.getDate() + 3);
	
	let endDay = new Date(now);
	endDay.setDate(now.getDate() - 1);
	endDay.setHours(23, 59, 59, 999);
	
	tds.forEach(t => {
		t.classList.remove("alert", "end");
		const td_date = new Date(t.textContent);
		if(alertDay >= td_date && td_date >= now) {
			t.classList.add("alert");
		} else if (td_date <= endDay) {
			t.classList.add("end");
		}
	});
}

//追加フォームーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
const addForm = document.getElementById('addForm');
const addTask = document.getElementById('task');
const addDate = document.getElementById('date');
addForm.addEventListener('submit', function(e) {
	e.preventDefault();

	fetch("AddServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams(new FormData(this))
	})
	.then(res => res.json())
	.then(res => {
		if (res.nullTaskDate) {
			addTask.classList.add('error');
			addDate.classList.add('error');
			alert(res.nullTaskDate);
			return;
		}
		if (res.nullTask) {
			addTask.classList.add('error');
			addDate.classList.remove('error');
			alert(res.nullTask);
			return;
		}
		if (res.nullDate) {
			addTask.classList.remove('error');
			addDate.classList.add('error');
			alert(res.nullDate);
			return;
		}
		newList();
		addForm.reset();
		addTask.classList.remove('error');
		addDate.classList.remove('error');
	});
});
//検索フォームーーーーーーーーーーーーーーー
const searchForm = document.getElementById('searchForm');
searchForm.addEventListener('submit', function(e) {
	e.preventDefault();

	fetch("SearchServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams(new FormData(this))
	})
	
	.then(res => res.json())
	.then(res => {
		if (res.error) {
			alert(res.error);
			return;
		}
		createTodoList(res);
	});
});
//並び替え 2ーーーーーーーーーーーーーー
const theadTh = document.querySelectorAll("thead th");	
const sessionData = document.getElementById("sessionData");//jspのセッションから並び順を取得
let sort  = sessionData.dataset.sort;
let order = sessionData.dataset.order;

theadTh.forEach(th => {
	th.addEventListener('click', function() {
		theadTh.forEach(t => {// 今の昇降順をリセット
			t.classList.remove("showUp", "showDown");
		})
		
		if(th.dataset.sort === sort) {// ソート状態とクリックした項目が同じなら反転、違うならASCを初期値
			order = (order === 'ASC') ? 'DESC' : 'ASC' ; 
		}else{
			sort = th.dataset.sort;
			order = 'ASC';
		}
		th.classList.add(order === 'ASC' ? 'showUp' : 'showDown')
		
		fetch("SortServlet", {
			method: "POST",
			headers: { "X-Requested-With": "XMLHttpRequest" },
			body: new URLSearchParams({ searchSort: sort, searchOrder: order })
		})
		.then(res => res.json())
		.then(createTodoList);
	});
});
//状態変化ーーーーーーーーーーーーーー1
function updateStatus(id, status) {
	fetch("StatusServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams({ id: id, status: status })
	})
	.then(() => newList());
}
//タスク変更ーーーーーーーーーーーーーー2
function editTaskBox(btn, id, task, date) {
	const td = btn.closest('td');
	td.innerHTML = `<input type="text" class="edit-task" value="${task}" autofocus onblur="updateTask(this, '${id}', this.value, '${date}')" onkeydown="if(event.key==='Enter'){ this.blur(); }">`;
}
function updateTask(btn, id, task, date) {
	const td = btn.closest('td');
	fetch("EditServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams({ id: id, task: task, date: date })
	})
	.then(res => res.json())
	.then(res => {
		if (res.nullTaskDate) {
			alert(res.nullTaskDate);
			return;
		}
		if (res.nullTask) {
			alert(res.nullTask);
			return;
		}
		if (res.nullDate) {
			alert(res.nullDate);
			return;
		}
		newList();
	});
}
//日付変更ーーーーーーーーーーーーーー3
function editDateBox(btn, id, task, date) {
	const td = btn.closest('td');
	td.innerHTML = `<input type="date" value="${date}" autofocus onchange="updateDate(this, '${id}', '${task}', this.value)">`;
}
function updateDate(btn, id, task, date) {
	const td = btn.closest('td');
	fetch("EditServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams({ id: id, task: task, date: date })
	})
	.then(res => res.json())
	.then(res => {
		if (res.nullTaskDate) {
			alert(res.nullTaskDate);
			return;
		}
		if (res.nullTask) {
			alert(res.nullTask);
			return;
		}
		if (res.nullDate) {
			alert(res.nullDate);
			return;
		}
		newList();
	});
} 
// TODO: 「削除」「todoList生成」で関数を分ける　〇
//削除ーーーーーーーーーーーーーー4
async function handleDeleteClick(id) {
	if (!confirm("削除しますか？")) {
		return;
	}
	const result = await deleteTodo(id);
	if(result) {
		alert("削除完了");
		newList();
	} else {
		alert("削除できませんでした");
	}
}
	
async function deleteTodo(id) {
	let res = await fetch("DeleteServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams({ id: id })
	})
	res = await res.json();
	return res.success === true;
}
//async   必ずPromiseを返す関数になる。
//await    Promiseが終わるのを待って結果を取り出す。asyncの中でしか使えない。

// 最新のtodoListーーーーーーーーーーーーーー
function newList() {
	fetch("TodoServlet", {
		method: "GET",
		headers: { "X-Requested-With": "XMLHttpRequest" }
	})
	.then(res => res.json())
	.then(createTodoList);
}
//サーブレット　→　res　→　todoListを生成ーーー検索と並び替えのみーーーーーーーーー;
function createTodoList (res) {
		const tbody = document.querySelector("tbody");
		tbody.innerHTML = ""; 
		res.forEach(r => {
			const tr = document.createElement("tr");
			tr.innerHTML = createHTML(r);
			tbody.appendChild(tr);
		});
		colorDates();
}
//タスクリスト表示HTMLーーーーーーーーーーーーーー;
function createHTML(t) {
	return `
		<td><input type="checkbox" name="status" value="true" ${t.status ? "checked" : ""} onchange="updateStatus(${t.id}, this.checked)"></td>
		<td>${t.id}</td>
		<td><button type="button" onclick="editTaskBox(this, ${t.id}, '${t.task}', '${t.date}')">${t.task}</button></td>
		<td><button class="date" type="button" onclick="editDateBox(this, ${t.id}, '${t.task}', '${t.date}')">${t.date}</button></td>
		<td><button type="button" onclick="handleDeleteClick(${t.id})"><i class="far fa-trash-alt fa-1x icon"></i></button></td>
	`
}

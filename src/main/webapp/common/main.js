'use strict'

//DOMContentLoaded 速い。HTMLが読み込み終わったら。
//window.onload 遅い。全読み込みが終わってから。(画像やcssなども)
//指定していくべき？ 21行と80行

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
//３日前、赤色ーーーーーーーーーーーーーーー
document.addEventListener('DOMContentLoaded', colorDates);
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
//ラジオボタンの解除ーーーーーーーーーーーーーーー
const radio = document.querySelectorAll('input[type="radio"]');
const ds = document.get
radio.forEach(r => {
	r.addEventListener('mousedown', function() {
		this.nowStatus = this.checked;
	});
	
	r.addEventListener('click', function() {
		if(this.nowStatus) {
			this.checked = false;
		}
	});
});
//矢印の向きと薄さを変えるーーーーーーーーーーーーーーー」
const searchStartDate = document.getElementById('searchStartDate');
const searchSingleDate = document.getElementById('searchSingleDate');
const searchEndDate = document.getElementById('searchEndDate');
const leftArrow = document.getElementById('leftArrow');
const rightArrow = document.getElementById('rightArrow');

[searchStartDate, searchEndDate].forEach(date => {
	date.addEventListener('click', function() {
		leftArrow.textContent = "　←　";
		rightArrow.textContent = "　→　";
		searchStartDate.classList.remove("show");
		searchSingleDate.classList.add("show");
		searchEndDate.classList.remove("show");
	});
});
searchSingleDate.addEventListener('click', function() {
	leftArrow.textContent = "　→　";
	rightArrow.textContent = "　←　";
	searchStartDate.classList.add("show");
	searchSingleDate.classList.remove("show");
	searchEndDate.classList.add("show");
});
//非同期初期画面ーーーーーーーーーーーーーーー
document.addEventListener("DOMContentLoaded", function() {
	if (window.location.pathname.endsWith("index2.jsp")) {
		newList();
	}
});

function newList() {
	fetch("TodoServlet", {
		method: "GET",
		headers: { "X-Requested-With": "XMLHttpRequest" }
	})
	
	.then(res => res.json()) //Promise<Response> → Promise<any>
	.then(res => {
		const tbody = document.querySelector("tbody");
		res.forEach(r => {
			const tr = document.createElement("tr");
			tr.innerHTML = createHTML(r);
			tbody.appendChild(tr);
		});
		colorDates();
	});
}
//非同期、検索フォームーーーーーーーーーーーーーーー
const searchForm = document.getElementById('searchForm');
searchForm.addEventListener('submit', function(e) {
	e.preventDefault();//他のデフォルトの動作をしない クリック後のリロードを防ぐ

	fetch("SearchServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },// Ajaxのリクエストを表すタグ
		body: new URLSearchParams(new FormData(this))   // "id=123&status=true" フォーム形式と同じになってサーブレットでparameterとして受け取れる。
//    URLSearchParams                                                              formを渡すとサーブレットが受け取れるapplication/x-www-form-urlencoded へ変換。
//    FormData                                                                           ブラウザ標準オブジェクト。formを渡すと入力内容をまとめたオブジェクトになる。
//    body: ({ id: id, status: status })                                        [object Object] ただのオブジェクト
//    body: JSON.stringify({ id: id, status: status })                  {"id":123,"status":"true"} JS用のキーと値
	})
	
	.then(res => res.json()) //レスポンスをjsonに
	.then(res => {
		if (res.error) {
			alert(res.error);
			return;
		}
		const tbody = document.querySelector("tbody");
		tbody.innerHTML = ""; 
		res.forEach(r => {
			const tr = document.createElement("tr");
			tr.innerHTML = createHTML(r);
			tbody.appendChild(tr);
		});
		colorDates();
	});
});
//非同期、並び替えフォームーーーーーーーーーーーーーー
const sortForm = document.getElementById('sortForm');
sortForm.addEventListener('submit', function(e) {
	e.preventDefault();

	fetch("SortServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams(new FormData(this))
	})
	
	.then(res => res.json())
	.then(res => {
		const tbody = document.querySelector("tbody");
		tbody.innerHTML = "";
		res.forEach(r => {
			const tr = document.createElement("tr");
			tr.innerHTML = createHTML(r);
			tbody.appendChild(tr);
		});
		colorDates();
	});
});
//非同期追加ーーーーーーーーーーーーーー
const addForm = document.getElementById('addForm');
addForm.addEventListener('submit', function(e) {
	e.preventDefault();

	fetch("AddServlet", {
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
		const tbody = document.querySelector("tbody");
		tbody.innerHTML = ""; 
		res.forEach(r => {
			const tr = document.createElement("tr");
			tr.innerHTML = createHTML(r);
			tbody.appendChild(tr);
		});
		colorDates();
		addForm.reset();
	});
});
//タスクリスト表示HTMLーーーーーーーーーーーーーー
function createHTML(t) {
	return `
		<td><input type="checkbox" name="status" value="true" ${t.status ? "checked" : ""} onchange="updateStatus('${t.id}', this.checked)"></td>
		<td>${t.id}</td>
		<td><button type="button" onclick="editTaskBox(this, '${t.id}', '${t.task}', '${t.date}')">${t.task}</button></td>
		<td><button class="date" type="button" onclick="editDateBox(this, '${t.id}', '${t.task}', '${t.date}')">${t.date}</button></td>
		<td><button type="button" onclick="deleteTodo(this, '${t.id}')"><i class="far fa-trash-alt fa-1x icon"></i></button></td>
	`;
}
//非同期、状態変化ーーーーーーーーーーーーーー
function updateStatus(id, status) {
	fetch("StatusServlet", {
		method: "POST",
		headers: { "X-Requested-With": "XMLHttpRequest" },
		body: new URLSearchParams({ id: id, status: status })
	});
}
//非同期タスク変更ーーーーーーーーーーーーーー
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
		if(res.error) {
			alert(res.error);
			editTaskBox(td, id, task, date);
			return;
		}
		td.innerHTML = `<button type="button" onclick="editTaskBox(this, '${id}', '${task}', '${date}')">${task}</button>`;
	});
}
//非同期日付変更ーーーーーーーーーーーーーー
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
		if(res.error) {
			alert(res.error);
			editDateBox(td, id, task, date);
			return;
		}
		td.innerHTML = `<button class="date" type="button" onclick="editDateBox(this, '${id}', '${task}', '${date}')">${date}</button>`;
		colorDates();
	});
} 
//非同期、削除ーーーーーーーーーーーーーー
function deleteTodo(btn, id) {
	if (!confirm("削除しますか？")) {
		return;
	}
	fetch("DeleteServlet", {
		method: "POST",
		headers: {"Content-Type": "application/x-www-form-urlencoded", "X-Requested-With": "XMLHttpRequest"}, //ヘッダーにフォーム形式を書くパターン
		body: `id=${id}`
	})
	.then(res => {
		if (res.ok) {
			const tr = btn.closest('tr');
			if (tr) {
				tr.remove();
			}
		}
	});
}

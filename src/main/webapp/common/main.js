'use strict'

// ヘッダーの li 要素を全部取得
const headerItems = document.querySelectorAll('ul li');

// クリックした li の位置を保存
headerItems.forEach((li, index) => {
  li.addEventListener('click', () => {
    sessionStorage.setItem('activeHeaderIndex', index);
  });
});

// リロード後に復元
document.addEventListener('DOMContentLoaded', () => {
  const savedIndex = sessionStorage.getItem('activeHeaderIndex');
  if (savedIndex === null) return;

  headerItems.forEach(li => li.classList.remove('show'));
  headerItems[savedIndex].classList.add('show');
});

//３日前、赤色に
document.addEventListener('DOMContentLoaded', highlightDates);
function highlightDates() {	
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

//ラジオボタンの解除
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


//矢印の向きと薄さを変える
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

//非同期初期画面
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
  
    .then(res => res.json())
    
    .then(todoList => {
      const tbody = document.querySelector("tbody");

      todoList.forEach(t => {
        const tr = document.createElement("tr");
        tr.innerHTML = createHTML(t);
        tbody.appendChild(tr);
      });

      highlightDates();
      
    })
    .catch(console.error);
}




//非同期、検索フォーム
const searchForm = document.getElementById('searchForm');
searchForm.addEventListener('submit', function(e) {
  e.preventDefault();//他のデフォルトの動作をしない クリック後のリロードを防ぐ

  fetch("SearchServlet", {
    method: "POST",
    headers: { "X-Requested-With": "XMLHttpRequest" },
    body: new URLSearchParams(new FormData(this))
//    URLSearchParams = formを渡すとサーブレットが受け取れるapplication/x-www-form-urlencoded へ変換。
//    FormData = ブラウザ標準オブジェクト。formを渡すと入力内容をまとめたオブジェクトになる。
  })
  
  .then(res => res.json())
  .then(todoList => {
	const tbody = document.querySelector("tbody");
    tbody.innerHTML = ""; 
    todoList.forEach(t => {
		const tr = document.createElement("tr");
		tr.innerHTML = createHTML(t);
      tbody.appendChild(tr);
    });
    highlightDates();
  }).catch(console.error);
});


//非同期、並び替えフォームーーーーーーーーーーーーーー
const sortForm = document.getElementById('sortForm');
sortForm.addEventListener('submit', function(e) {
e.preventDefault();
console.log('ここまで');

  fetch("SortServlet", {
    method: "POST",
    headers: { "X-Requested-With": "XMLHttpRequest" },
    body: new URLSearchParams(new FormData(this))
  })
  
  .then(res => res.json())
  .then(todoList => {
	const tbody = document.querySelector("tbody");
    tbody.innerHTML = "";
    todoList.forEach(t => {
		const tr = document.createElement("tr");
		tr.innerHTML = createHTML(t);
      tbody.appendChild(tr);
    });
    highlightDates();
  }).catch(console.error);
});


//非同期、状態変化ーーーーーーーーーーーーーー
function updateStatus(id, status) {
  fetch("StatusServlet", {
    method: "POST",
    headers: { "X-Requested-With": "XMLHttpRequest" },
    body: new URLSearchParams({ id: id, status: status })
  }).then(r => r.text())
    .then(console.log)
    .catch(console.error);
}

//非同期、削除ーーーーーーーーーーーーーー
function deleteTodo(btn, id) {
  if (!confirm("削除しますか？")) {
	  return;
	  }
	  console.log(id);  
  fetch("DeleteServlet", {
    method: "POST",
    headers: {"Content-Type": "application/x-www-form-urlencoded", "X-Requested-With": "XMLHttpRequest"},
    body: `id=${id}`
  }).then(r => {
    if (r.ok) {
	    const tr = btn.closest('tr');
	    if (tr) {
			tr.remove();
		}      
		console.log('削除処理 成功');
    }
  }).catch(console.error);
}



//タスクリスト表示HTMLーーーーーーーーーーーーーー
function createHTML(t) {
	return `
		<td><input type="checkbox" name="status" value="true" ${t.status ? "checked" : ""} onchange="updateStatus('${t.id}', this.checked)"></td>
		<td>${t.id}</td>
		<td><button type="button" onclick="editTaskBox(this, '${t.id}', '${t.task}', '${t.date}')">${t.task}</button></td>
		<td><button class="date" type="button" onclick="editDateBox(this, '${t.id}', '${t.task}', '${t.date}')">${t.date}</button></td>
		<td><button type="button" onclick="deleteTodo(this, '${t.id}')"><i class="far fa-trash-alt fa-1x icon"></i></button></td>
	`;
		//<td><button id="date" type="button" onclick="editDateBox(this, '${t.id}', '${t.task}', '${t.date}')">${t.date}</button></td>
}
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
  .then(todoList => {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = ""; 
    todoList.forEach(t => {
      const tr = document.createElement("tr");
      tr.innerHTML = createHTML(t);
      tbody.appendChild(tr);
    });
    highlightDates();
    addForm.reset();
  })
  .catch(console.error);
});



//非同期日付変更ーーーーーーーーーーーーーー
function editDateBox(btn, id, task, date) {
	btn.closest('td').innerHTML = `
		<input type="date" value="${date}" autofocus onchange="updateDate(this, '${id}', '${task}', this.value)">
	`;
}
function updateDate(btn, id, task, date) {
	console.log("編集関数までは来た");
	fetch("EditServlet", {
		method: "POST",
	    headers: { "X-Requested-With": "XMLHttpRequest" },
	    body: new URLSearchParams({ id: id, task: task, date: date })
		
	}).then(r => {
		if(r.ok) {
			console.log("タスク内容変更完了");
		}
	});
	btn.closest('td').innerHTML = `
		<button class="date" type="button" onclick="editDateBox(this, '${id}', '${task}', '${date}')">${date}</button>
	`;
		//<button id="date" type="button" onclick="editDateBox(this, '${id}', '${task}', '${date}')">${date}</button>
	highlightDates();
}

//非同期タスク変更ーーーーーーーーーーーーーー
function editTaskBox(btn, id, task, date) {
	btn.closest('td').innerHTML = `
		<input type="text" class="edit-task" value="${task}" autofocus onblur="updateTask(this, '${id}', this.value, '${date}')" onkeydown="if(event.key==='Enter'){ this.blur(); }">
	`;
}
function updateTask(btn, id, task, date) {
	console.log("編集関数までは来た");
	fetch("EditServlet", {
		method: "POST",
	    headers: { "X-Requested-With": "XMLHttpRequest" },
	    body: new URLSearchParams({ id: id, task: task, date: date })
		
	}).then(r => {
		if(r.ok) {
			console.log("タスク内容変更完了");
		}
	});
	btn.closest('td').innerHTML = `
		<button type="button" onclick="editTextBox(this, '${id}', '${task}', '${date}')">${task}</button>
	`;
}

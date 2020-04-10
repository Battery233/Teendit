(function() {
	/**
	 * Initialize major event handlers
	 */
	function init() {
		document.querySelector('#login-btn').addEventListener('click', login);
	      validateSession();
	}
	
	
	
	
	function validateSession() {
	    onSessionInvalid();
	    // The request parameters
	    var url = './login';
	    var req = JSON.stringify({});
	    fetch(url, {
	    	method: 'GET'
	    })
	    .then(res => {
	    	return res.json();
	    })
	    .then(res => {
	    	if (res.status === 'OK') {
	    		onSessionValid(res);
	    	}
	    }).catch(function(){
	    })
	    
	  }

	  function onSessionValid(res) {
	    user_id = res.user_id;
//	    user_fullname = res.name;

	    var loginContent = document.querySelector('#login-content');
	    var globalContent = document.querySelector('#globalstream-content');


	    hideElement(loginContent);
	    showElement(globalContent);

	    loadItems();
	  }
	  
	  
	  
	  
	  function onSessionInvalid() {
		    var loginContent = document.querySelector('#login-content');
		    var globalContent = document.querySelector('#globalstream-content');

//		    var registerForm = document.querySelector('#register-form');
//		    var itemList = document.querySelector('#container');
//		    var avatar = document.querySelector('#avatar');
//		    var welcomeMsg = document.querySelector('#welcome-msg');
//		    var logoutBtn = document.querySelector('#logout-link');

		    hideElement(globalContent);
		    showElement(loginContent);

//		    clearLoginError();
//		    showElement(loginForm);
		  }
	
	
	function hideElement(element) {
			element.style.display = 'none';
	}

	function showElement(element, style) {
		var displayStyle = style ? style : 'block';
		element.style.display = displayStyle;
	}
			 

	function login() {
		var username = document.querySelector('#txtUserName').value;
		var password = document.querySelector('#txtPassword').value;
//		password = md5(username + md5(password));

		// The request parameters
		var url = './login';
		var req = JSON.stringify({
			user_id : username,
			password : password,
		});

		console.log(req);
		fetch(url, {
			method: 'POST',
			body: req
		})
			.then(res => {
				if (res.status !== 200) {
					console.log(res);
					console.log("RETURN VALUE NOT 200!");
					throw new Error("not 200");
				} else{
					console.log(res);
					console.log("Status 200!");
				}
				return res.json()
			})
			.then(res => {
				if (res.status === 'OK') {
					onSessionValid(res);
				} else{
					alert("Login error!");
				}
			})
			.catch(err => {
				console.log("not 200 error!");
			})
	}
	
	function loadItems() {
		var url = './main';
		var data = null;
		// get the JSON array from the server.
		fetch(url + '?' + params, {
			method: 'GET',
		})
		.then(res => {
			return res.json();
		})
		.then(items => {
			console.log(items);
			// record the item and print to the webpage
			if (!items || items.length === 0) {
			} else {
				listItems(items);
			}
		})
		.catch(err => {
			console.error(err);  //print error
		})
	}
	
	/**
	 * render all the items on the web page.
	 */
	function listItems(items) {
	    var itemList = document.querySelector('#item-list');
	    itemList.innerHTML = ''; // clear current results

	    for (var i = 0; i < items.length; i++) {
	      addItem(itemList, items[i]); //List all the content
	    }
	  }
	
	/**
	 * render one item on the web page.
	 */
	function addItem(itemList, item) {
		var item_id = item.item_id;
		
		// create a new <li> tag.
		var li = $create('li', {
		      id: 'item-' + item_id,
		      className: 'item'
		    });
		
		li.dataset.item_id = item_id; // set id
	    
		// set edit and delete icons.
	    var edit_section = $create('span', {
	    	className: 'edit'
	      });
	    var edit = $create('i', {
	        className: 'fa fa-edit'
	      });
	    edit_section.appendChild(edit);
	    var trash_section = $create('span', {
	    	className: 'delete'
	      });
	    var trash = $create('i', {
	        className: 'fa fa-trash-o'
	      });
	    trash_section.appendChild(trash);
	    li.innerHTML = item.content;
	    
	    // append edit and delete icons.
	    li.appendChild(edit_section);
	    li.appendChild(trash_section);
	    itemList.appendChild(li);
	}

	init();
}) ();

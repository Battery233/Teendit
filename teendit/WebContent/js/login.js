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
				if (res.status === 401) {
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
				}
			})
			.catch(err => {
				alert("Login error!");
				console.log("not 200 error!");
			})
	}
	
	function loadItems() {
		var url = './main';
		var data = null;
		// get the JSON array from the server.
		fetch(url, {
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
        var s="<div class=\"news-list-item clearfix\"style=\"padding-bottom: 20px; border-bottom: 1px solid #eee\"><div class\=\"row\"><div class=\"col-xs-8\"><div><a href=\"#\" class=\"title\"style=\"display: block; color: #444; font-size: 18px; font-weight: bold; margin-bottom: 5px; line-height: 1.5\">%s</a></div><p>%s</p><div class=\"info\"><a>%s</a><span>%s</span></div></div></div></div>"
        	s=s.format("Title"+item.item_id,item.content,item.user_id,"fake date");
        
        var child = document.createElement('div');
        child.innerHTML = s;
        child = child.firstChild;
        itemList.appendChild(child);

	}
	
	String.prototype.format= function(){
		    //将arguments转化为数组（ES5中并非严格的数组）
		    var args = Array.prototype.slice.call(arguments);
		    var count=0;
		    //通过正则替换%s
		    return this.replace(/%s/g,function(s,i){
		        return args[count++];
		    });
		}


	init();
}) ();

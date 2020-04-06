var user_id = '1111';    // default user_id
/**
 * Add function. Use this function to add new to-do item
 * 
 */
$("#content").on("keypress", function(){
	if(event.which == 13){ // action when enter is pressed
		if ($(this).val() === '') {
			alert("Please write something!");   // alert if there is no input.
        } else {
        	// append a new to-do record.
        	$("ul").append("<li>" + $(this).val() +  "<span class = 'edit'><i class='fa fa-edit'></i></span><span class = 'delete'><i class='fa fa-trash-o'></i></span></li>");
        	//var user_id = "1111"; //Default user id
        	var content = $(this).val();
        	
        	// build JSON file
        	var req = JSON.stringify({
        	      user_id: user_id,
        	      content: content
        	    });
        	var url = './main';
    		var params = 'user_id=' + user_id;
    		
    		//Send JSON to the server, use POST method
    		fetch(url + '?' + params, {
    			method: 'POST',
    			body: req
    		}).then(function(response) {
    			return response.json;
    		})
        }
		
		$(this).val(""); //Clear the in-box after the operations
	}
});

/**
 * Delete function. Use this function to delete a to-do item
 * 
 */
$(document).on("click", "li span.delete", function(){
	$(this).parent().fadeOut(function(){  //set visual effects
		var url = './main';
		//var user_id = "1111";  // default user
		//set payload for the JSON
		var params = 'user_id=' + user_id;
		var item_id = $(this).attr("data-item_id");
    	var content = $(this).val();
    	var req = JSON.stringify({
    		  item_id: item_id,
    	      user_id: user_id,
    	      content: content
    	    });
    	//Send JSON to the server and remove the record
		fetch(url + '?' + params, {
			method: 'DELETE',
			body: req
		}).then(function(response) {
			return response.json;
		})
		//remove the existed item.
		$(this).remove();
	});
});

/**
 * Show the modify and delete icons.
 * 
 */
$("h1 i").click(function(){
	$(".slide").slideToggle();	//slide effects
});

/**
 * Modify function. Use this function to modify a to-do item
 * 
 */
$(document).on("click", "li span.edit", function(){
		var liInputId = $(this).parent("li").attr("id") + '-input';
		// change the to-list to input row.
		$(this).parent("li").html('<input type="text"' + ' id=' + liInputId + ' ' +'value="' + $(this).parent("li").text() + '">');
		// catch user's input
		$("#" + liInputId).on("keypress", function(){
		if (event.which == 13){ // When receive an enter key press
          var item_id = $(this).parent().attr("data-item_id");
          var url = './main';
  		  //var user_id = "1111"; //Default user
  		  var params = 'user_id=' + user_id;
  		  var content = $(this).val();
  		//set payload for the JSON
  		  var req = JSON.stringify({
  			  item_id: item_id,
  			  user_id: user_id,
  			  content: content
  		  	});
  		  //Send JSON to the server.
  		  fetch(url + '?' + params, {
  			  method: 'POST',
  			  body: req
  		  }).then(function(response) {
  			  return response.json;
  		  })
  		  // append modified item to the lists.
		  $(this).parent("li").html($(this).val() + '<span class = "edit"><i class="fa fa-edit"></i></span><span class = "delete"><i class="fa fa-trash-o"></i></span>');
		}    
    });
});

(function() {
	//var user_id = '1111';    // default user_id

	/**
	   * Initialize major event handlers
	   */
	function init() {
	  // register event listeners
	  document.querySelector('#login-form-btn').addEventListener('click', onSessionInvalid);
	  document.querySelector('#login-btn').addEventListener('click', login);
	  document.querySelector('#register-form-btn').addEventListener('click', showRegisterForm);    
	  document.querySelector('#register-btn').addEventListener('click', register);
	  //document.querySelector('#container').addEventListener('click', loadNearbyItems);
      validateSession();
	    // onSessionValid({"user_id":"1111","name":"John Smith","status":"OK"});
	}

	/**
	 * Session
	 */
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
	    })
	    
	  }

	  function onSessionValid(res) {
	    user_id = res.user_id;
	    user_fullname = res.name;

	    var loginForm = document.querySelector('#login-form');
	    var registerForm = document.querySelector('#register-form');
	    var itemList = document.querySelector('#container');
	    var avatar = document.querySelector('#avatar');
	    var welcomeMsg = document.querySelector('#welcome-msg');
	    var logoutBtn = document.querySelector('#logout-link');

	    welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

	    showElement(itemList);
	    showElement(avatar);
	    showElement(welcomeMsg);
	    showElement(logoutBtn, 'inline-block');
	    hideElement(loginForm);
	    hideElement(registerForm);

	    loadItems();
	    //initGeoLocation();
	  }

	  function onSessionInvalid() {
	    var loginForm = document.querySelector('#login-form');
	    var registerForm = document.querySelector('#register-form');
	    var itemList = document.querySelector('#container');
	    var avatar = document.querySelector('#avatar');
	    var welcomeMsg = document.querySelector('#welcome-msg');
	    var logoutBtn = document.querySelector('#logout-link');

	    hideElement(itemList);
	    hideElement(avatar);
	    hideElement(logoutBtn);
	    hideElement(welcomeMsg);
	    hideElement(registerForm);

	    clearLoginError();
	    showElement(loginForm);
	  }
	  
	  function hideElement(element) {
		element.style.display = 'none';
	  }

	  function showElement(element, style) {
		 var displayStyle = style ? style : 'block';
		 element.style.display = displayStyle;
	  }
		  
	   function showRegisterForm() {
		 var loginForm = document.querySelector('#login-form');
		 var registerForm = document.querySelector('#register-form');
		 var itemList = document.querySelector('#container');
		 var avatar = document.querySelector('#avatar');
		 var welcomeMsg = document.querySelector('#welcome-msg');
		 var logoutBtn = document.querySelector('#logout-link');

	     hideElement(itemList);
         hideElement(avatar);
	     hideElement(logoutBtn);
    	 hideElement(welcomeMsg);
		 hideElement(loginForm);
		    
		 clearRegisterResult();
		 showElement(registerForm);
	   } 
	
	   
	// -----------------------------------
	// Login
	// -----------------------------------

	   function login() {
		 //e.preventDefault();
	     var username = document.querySelector('#username').value;
	     var password = document.querySelector('#password').value;
	     password = md5(username + md5(password));

	     // The request parameters
	     var url = './login';
	     var req = JSON.stringify({
	       user_id : username,
	       password : password,
	     });
	     
	     fetch(url, {
	    	   method: 'POST',
	    	   body: req
	       })
	       .then(res => {
	    	   if (res.status !== 200) {
	    		   throw new Error("not 200");
	    	   }
	    	   return res.json()
	       })
	       .then(res => {
	    	   if (res.status === 'OK') {
	    		   onSessionValid(res);
	    	   }
	       })
	       .catch(err => {
	    	   console.log(1);
	    	   showLoginError();
	       })
	   }

	   function showLoginError() {
	     document.querySelector('#login-error').innerHTML = '<div class = "bar error">Invalid username or password</div>';
	   }

	   function clearLoginError() {
	     document.querySelector('#login-error').innerHTML = '';
	   }
	   
	   
	// -----------------------------------
	// Register
	// -----------------------------------

	   function register() {
	     var username = document.querySelector('#register-username').value;
	     var password = document.querySelector('#register-password').value;
	     var firstName = document.querySelector('#register-first-name').value;
	     var lastName = document.querySelector('#register-last-name').value;
	     
	     if (username === "" || password == "" || firstName === "" || lastName === "") {
	     	showRegisterResult('<div class="bar error">Please fill in all fields</div>');
	     	return
	     }
	     
	     if (username.match(/^[a-z0-9_]+$/) === null) {
	     	showRegisterResult('<div class="bar error">Invalid username</div>');
	     	return
	     }
	     
	     password = md5(username + md5(password));

	     // The request parameters
	     var url = './register';
	     var req = JSON.stringify({
	       user_id : username,
	       password : password,
	       first_name: firstName,
	       last_name: lastName,
	     });
	     
	     fetch(url, {
	    	 method: 'POST',
	    	 body: req
	     })
	     .then(res => {
	    	 return res.json();
	     })
	     .then(res => {
	    	 if (res.status === 'OK') {
	    		 showRegisterResult('<div class="bar error">Succesfully registered</div>');
	    	 } else {
	    		 showRegisterResult('<div class="bar error">User already existed</div>');
	    	 }
	     })
	     .catch(err => {
	    	 showRegisterResult('<div class="bar error">Failed to register</div>');
	     })
	     
	   }

	   function showRegisterResult(registerMessage) {
	     document.querySelector('#register-result').innerHTML = registerMessage;
	   }

	   function clearRegisterResult() {
	     document.querySelector('#register-result').innerHTML = '';
	   }
	
	/**
	 * Use to create a new DOM element
	 * @return new DOM element
	 */
	function $create(tag, options) {
		  var element = document.createElement(tag);
		  for (var key in options) {
		    if (options.hasOwnProperty(key)) {
		      element[key] = options[key];
	        }
		  }
		    return element;
	  }
	/**
	 * Show the warning message.
	 */
	function showWarningMessage(msg) {
	    var itemList = document.querySelector('#item-list');
	    itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> ' +
	      msg + '</p>'; //print the message
	  }
	/**
	 * Show the error message.
	 */
	function showErrorMessage(msg) {
		  var itemList = document.querySelector('#item-list');
		  itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> ' +
		    msg + '</p>';  //print the message
	   }
	
	/**
	 * Add all existed to-do items to the web page.
	 */
	function loadItems() {
		var url = './main';
		var params = 'user_id=' + user_id;
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
	      addItem(itemList, items[i]); //List all the todo content
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
	//loadItems();
}) ();
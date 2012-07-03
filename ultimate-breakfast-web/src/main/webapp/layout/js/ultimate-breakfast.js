/** Global item repository for UltimateBreakfast js */
if(!UltimateBreakfast)
{
	var UltimateBreakfast = {};
}

/** JsConsole */

/** 
 * Constructor
 * @class Represents JsConsole component. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the JsConsole element
 * @param {String} url the url used to execute AJAX request.
 * @param {int} historySize the size of the history
 * @param {Array} confirmCommands commands to be confirmed
 * @param {String} motd Message of the day
 */
UltimateBreakfast.JsConsole = function(id, url, historySize, confirmCommands, motd) {
	/**
	 * Id of the JsConsole
	 */
	this.id = id;
	
	/**
	 * Used to execute AJAX request for launching commands from JsConsole
	 */ 
	this.url = url;
	
	/**	
	 * Current request
	 */
	this.request = null;
	
	/**
	 * Console History
	 */
	this.history = {
			items: [],
			capacity: historySize,
			current: 0
	};
	
	/**
	 * Commands Stack
	 */
	this.stack = [];
	
	/**
	 * List of commands who needs confirm action from user 
	 */
	this.confirmCommands = confirmCommands;
	
	/**
	 * motd message
	 */
	this.motd = motd;
};

/**
 * CSS Selector prefix used by JsConsole
 * @constant
 * @static
 * @private
 */
UltimateBreakfast.JsConsole.CSS_SELECTOR_PREFIX = "jsconsole-";

/**
 * Request parameter used to send the command server-side
 * @see Java JsConsole.PARAM_COMMAND_NAME
 * @constant
 * @static
 * @private
 */
UltimateBreakfast.JsConsole.PARAM_COMMAND_NAME = "command";

/**
 * Confirm yes param
 * @private
 */
UltimateBreakfast.JsConsole.CONFIRM_YES = "y";

/**
 * Console output prefix for error message
 * @private
 */
UltimateBreakfast.JsConsole.OUTPUT_PREFIX_ERROR = "  ==> ";

/**
 * Request parameter used to send the command args
 * @see Java JsConsole.PARAM_COMMAND_NAME
 * @constant
 * @static
 * @private
 */
UltimateBreakfast.JsConsole.PARAM_ARGS_NAME = "args";

/**
 * Method to get the JsConsole input DOM element
 * @return {HTMLElement} the input element of the JsConsole
 * @private 
 */
UltimateBreakfast.JsConsole.prototype.getInputEl = function() {
	return YAHOO.util.Dom.get("input-" + this.id);
};

/**
 * Method to get the JsConsole output DOM element
 * @return {HTMLElement} the output element of the JsConsole
 * @private 
 */
UltimateBreakfast.JsConsole.prototype.getOutputEl = function() {
	return YAHOO.util.Dom.get("output-" + this.id);
};

/**
 * Push a command into history
 * @private
 */
UltimateBreakfast.JsConsole.prototype.pushHistory = function(command) {
	if (this.history.items.indexOf(command) < 0) {
		this.history.items.unshift(command);
	}
	if (this.history.items.length > this.history.capacity) {
		this.history.items.pop();
	}
};

/**
 * Reset the current item of the history
 * @private
 */
UltimateBreakfast.JsConsole.prototype.resetHistory = function() {
	this.history.current = 0;
};

/**
 * Fetch item from history
 */
UltimateBreakfast.JsConsole.prototype.getHistoryItem = function() {
	var cursor = this.history.current;
	var len = this.history.items.length;
	if (cursor > len || cursor > this.history.capacity) {
		cursor = cursor - 1;
	}
	else if (cursor < 0) {
		cursor = 0;
	}
	var item = this.history.items[cursor - 1];
	this.history.current = cursor;
	if (item) {
		this.setConsoleInput(item);
	}
	else {
		this.clearConsoleInput();
	}
};

/**
 * Move the position of the cursor into the textarea
 * @private
 */
UltimateBreakfast.JsConsole.prototype.moveCursorToTheEnd = function() {
	var el = this.getInputEl();
	var len = el.value.length;
	if (el.setSelectionRange) {
		el.focus();
		el.setSelectionRange(len, len);
	}
	else {
		if (el.createTextRange) {
			range=el.createTextRange();
			range.collapse(true);
			range.moveEnd('character', len);
			range.moveStart('character', len);
			range.select();
		}
	} 
};


/**
 * Initialize JsConsole component
 * @private
 */
UltimateBreakfast.JsConsole.prototype._init = function() {
	var jsconsole = this;
	
	// Callback for Enter key
	var callbackEnter = function(e) {
		jsconsole.parseConsoleInput();
	};
	
	// Callback for Up and Down key
	var callbackUp = function(e) {
		jsconsole.history.current++;
		jsconsole.getHistoryItem();
	};
	var callbackDown = function(e) {
		jsconsole.history.current--;
		jsconsole.getHistoryItem();
	};
	var callbackKeyUp = function(e) {
		jsconsole.moveCursorToTheEnd();
	};
	
	// Clear console
	this.clearConsoleOutput();

	// Key listeners
	var keyEnterHandle = new YAHOO.util.KeyListener(this.getInputEl(), {keys:YAHOO.util.KeyListener.KEY.ENTER}, callbackEnter);
	var keyUpHandle = new YAHOO.util.KeyListener(this.getInputEl(), {keys:YAHOO.util.KeyListener.KEY.UP}, callbackUp);
	var keyDownHandle = new YAHOO.util.KeyListener(this.getInputEl(), {keys:YAHOO.util.KeyListener.KEY.DOWN}, callbackDown);
	var specialHandle = new YAHOO.util.KeyListener(this.getInputEl(), {keys:[YAHOO.util.KeyListener.KEY.UP, YAHOO.util.KeyListener.KEY.DOWN]}, callbackKeyUp, 'keyup');
	
	// Enable key listeners
	keyEnterHandle.enable();
	keyUpHandle.enable();
	keyDownHandle.enable();
	specialHandle.enable();
	
	// Display motd
	if (this.motd) {
		this.writeLine("", this.motd, "command-motd");
	}
};	

/**
 * Set a value into the console input
 */
UltimateBreakfast.JsConsole.prototype.setConsoleInput = function(input) {
	this.getInputEl().value = input;
};

/**
 * Clear the console input
 */
UltimateBreakfast.JsConsole.prototype.clearConsoleInput = function() {
	this.setConsoleInput("");
	this.getInputEl().focus();
};

/**
 * Clear the console output
 */
UltimateBreakfast.JsConsole.prototype.clearConsoleOutput = function() {
	this.getOutputEl().innerHTML = "";
};

/**
 * Clear the console output and the console input
 */
UltimateBreakfast.JsConsole.prototype.clear = function() {
	this.clearConsoleOutput();
	this.clearConsoleInput();
};

UltimateBreakfast.JsConsole.prototype.writeLine = function(prefix, text, css)
{
	var output = this.getOutputEl();
	// Create new element and input command as text node
	var span = document.createElement("span");
	var inputText = document.createTextNode(prefix + text);
	if (css) {
		YAHOO.util.Dom.addClass(span, css);	
	}
	else {
		YAHOO.util.Dom.addClass(span, "command");	
	}
	span.appendChild(inputText);
	output.appendChild(span);
};

UltimateBreakfast.JsConsole.prototype.refreshConsoleOutput = function(input, result, css)
{
	// Write input
	if (input) {
		this.writeLine("> ", input);
	}
	
	// Write output
	var output = this.getOutputEl();	
	if (result) {
		var className = "response";
		if (css) {
			className = css;
		}
		var prefix = css == 'command-error' ? UltimateBreakfast.JsConsole.OUTPUT_PREFIX_ERROR : '';
		for(var i=0; i < result.length; i++){
			this.writeLine(prefix, result[i], className);
		}
		
		// Scroll down
		output.scrollTop = output.scrollHeight;
	}
	
	this.clearConsoleInput();
};

/**
 * Parse the command line
 * @private 
 */
UltimateBreakfast.JsConsole.prototype.parseCommandLine = function(command) {
	var resultArray = command.match(/"[^"]+"|'[^']+'|\S+/g)
	var i = resultArray.length;
	while(i--){
		resultArray[i] = resultArray[i].replace(/"/g,"");
		var match = /.+'.+?/.exec(resultArray[i]);
		if (!match) {
			resultArray[i] = resultArray[i].replace(/'/g,"");
		}
	}

	return resultArray;
};

/**
 * Parse the command before execute
 */
UltimateBreakfast.JsConsole.prototype.parseConsoleInput = function() {
	var input = this.getInputEl().value.replace(/\n/g, '');
	
	if (input) {
		var handleHistory = true;
		// Special commands
		if (input == 'clear') {
			this.clear();
		}
		else {			
			if (this.stack.length == 1) {
				var realCommand = this.stack[0];
				if (YAHOO.lang.trim(input) == UltimateBreakfast.JsConsole.CONFIRM_YES) {
					
					this.executeCommand(realCommand);
				}
				else {
					this.writeLine(UltimateBreakfast.JsConsole.OUTPUT_PREFIX_ERROR, "Command aborted!", "command-error");
					this.clearConsoleInput();
				}
				this.stack.pop();
				handleHistory = false;
			}
			else {
				this.executeCommand(input);
			}
		}
		
		// Handle history
		if (handleHistory) {
			this.pushHistory(input);
			this.resetHistory();
		}
	}
};

UltimateBreakfast.JsConsole.prototype.hasCommandToBeConfirmed = function(command) {
	return this.confirmCommands.indexOf(command) >= 0;
};

/**
 * Execute the Ajax query responsible for launching the jsconsole command to the server.
 */
UltimateBreakfast.JsConsole.prototype.executeCommand = function(input)
{
	var jsconsole = this;
	
	// Parse input command
	var commandArray = this.parseCommandLine(input);
	var command = commandArray.shift();
	var args = YAHOO.lang.JSON.stringify(commandArray);
	
	// Has command to be confirmed ?
	if (this.stack.length == 0 && this.hasCommandToBeConfirmed(command)) {
		this.stack.push(input);
		this.refreshConsoleOutput(input, ["Are you sure ? [y/n]"], "command-confirm");
		return;
	}

	// Ajax Failure handler
	var failureHandler = function(o) 
	{
		jsconsole.request = null;
	};
	
	// Ajax Success handler
	var successHandler = function(o) 
	{
		var result = YAHOO.lang.JSON.parse(o.responseText);
		if (jsconsole.hasCommandToBeConfirmed(command)) {
			input = "";
		}
		var css = result.status == 'error' ? 'command-error' : '';
		jsconsole.refreshConsoleOutput(input, result.response, css);
	};
	
	// Callback objects
	var callback = 
	{
		success: successHandler,
		failure: failureHandler,
		cache: false
	};	
	
	var url = this.url 
				+ "?" + UltimateBreakfast.JsConsole.PARAM_COMMAND_NAME + "=" + command 
				+ "&" + UltimateBreakfast.JsConsole.PARAM_ARGS_NAME + "=" + args;
	
	// Execute Ajax request
	var request = YAHOO.util.Connect.asyncRequest(
			"GET",
			url, 
			callback, 
			null
	);

	// Ajax protection
	if(YAHOO.util.Connect.isCallInProgress(this.request)){
	    YAHOO.util.Connect.abort(request);
	}
	else {
		this.request = request;
	}
};

/**
 * Initializes a JS Console component on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @static
 * @private
 */
Tapestry.Initializer.jsConsoleBuilder = function(data){
	var jsConsole = new UltimateBreakfast.JsConsole(data.id, data.url, data.historySize, YAHOO.lang.JSON.parse(data.confirmCommands), data.motd);
	window[data.id] = jsConsole;
	jsConsole._init();
};

UltimateBreakfast.placeholderOldBrowserSupport = function() {
	if (!Modernizr.input.placeholder)
    {	
		// Special handler for password fields
		var passwordFields = YAHOO.util.Dom.getElementsByClassName('placeholder');
		for(var i = 0; i < passwordFields.length; i++) {
			var field = passwordFields[i];
			var span = document.createElement("span");
			var inputText = document.createTextNode(field.placeholder);

			YAHOO.util.Dom.addClass(span, 'placeholderField');	
			span.appendChild(inputText);
			
			var region = YAHOO.util.Dom.getRegion(field);
			
			YAHOO.util.Dom.setStyle(span, 'top', region.top + 'px');
			YAHOO.util.Dom.setStyle(span, 'left', region.left + 'px');
			var body = document.body;
			body.appendChild(span);
			
			YAHOO.util.Event.on(field, "focusin", function(e) {
				var el = YAHOO.util.Event.getTarget(e);
				YAHOO.util.Dom.setStyle(this, 'display', 'none');	
			}, span, true);
			
			YAHOO.util.Event.on(field, "focusout", function(e) {
				var el = YAHOO.util.Event.getTarget(e);
				if (el.value == '' || el.value == null) {
					YAHOO.util.Dom.setStyle(this, 'display', 'block');
				}
			}, span, true);
		}
		
    }
};

Tapestry.Initializer.ultimateBreakfastInitializer = function(data){
	// Support for placeholder with older browsers
	var placeholderSupport = new UltimateBreakfast.placeholderOldBrowserSupport();
};

Tapestry.Initializer.oldBrowserMessageBuilder = function(){
	if(!Modernizr.input.placeholder){
		document.getElementById('oldBrowserMessage').style.display='';
	}
};

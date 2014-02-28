spec(function() {
	/**
	 * Test Configuration
	 */
	var configuration = {
		// NOTE: Update this to an existing Telerik Backend Services username and password
		USERNAME: 'seth',
		PASSWORD: '333333',

		// NOTE: Update this to the iOS URL of the Friends App
		IOS_URL: 'friends://',

		// NOTE: Update this to the Android package name of the Friends App
		ANDROID_PACKAGE: 'com.telerik.app'
	};

	/**
	 * Queries
	 * These are reusable queries for elements in the user interface.
	 */
	var queries = {
		ios: {
			globals: {
				navigationBar: { class: 'UINavigationBar' }
			},
			login: {
				usernameField: { class: 'UITextField', properties: { placeholder: 'Username' } },
				passwordField: { class: 'UITextField', properties: { placeholder: 'Password' } },
				loginButton: { class: 'TKUIButton', "text": 'Login' }
			},
			activities: {
				tableView: { class: 'UITableView' },
				addButton: { class: 'UIButton', index: 1 },
				logoutButton: { class: 'UIButton', properties: { currentTitle: 'Log out'} }
			},
			activity: {
				textView: { class: 'UITextView' },
				postButton: { class: 'UINavigationButton', properties: { 'currentTitle': 'Post' } }
			}
		},
		android: {
			login: {
				usernameField: { class: 'android.widget.EditText', properties: { 'hint': 'Username' } },
				passwordField: { class: 'android.widget.EditText', properties: { 'hint': 'Password' } },
				loginButton: { class: 'android.widget.Button', properties: { 'text': 'Login' } }
			},
			activities: {
				addButton: { class: 'com.android.internal.view.menu.ActionMenuItemView' },
			},
			activity: {
				textView: { class: 'android.widget.EditText' },
				postButton: { class: 'com.android.internal.view.menu.ActionMenuItemView' }
			}
		}
	};

	/**
	 * Step Repository
	 * These are reusable steps that perform actions in the application under test.
	 */
	var stepRepository = {
		'Given Friends is running': {
			'ios': [
				ios.launch(configuration.IOS_URL)
			],
			'android': [
				android.launch(configuration.ANDROID_PACKAGE),
				android.wait(1000)
			]
		},
		'And is logged in': {
			'ios': [
				ios.setText(queries.ios.login.usernameField, configuration.USERNAME),
				ios.setText(queries.ios.login.passwordField, configuration.PASSWORD),
				ios.tap(queries.ios.login.loginButton),
				ios.wait(3000)
			],
			'android': [
				android.setText(queries.android.login.usernameField, configuration.USERNAME),
				android.setText(queries.android.login.passwordField, configuration.PASSWORD),
				android.tap(queries.android.login.loginButton),
				android.wait(3000)
			]
		},
		'When add is tapped': {
			'ios': [
				ios.wait(2000),
				ios.tap(queries.ios.activities.addButton)
			],
			'android': [
				android.wait(2000),
				android.tap(queries.android.activities.addButton),
				android.wait(1000)
			]
		},

		'And something on my mind is input': {
			'ios': [
				ios.typeText(queries.ios.activity.textView, 'Hello World')
			],
			'android': [
				android.setText(queries.android.activity.textView, 'Hello World'),
				android.wait(1000)
			]
		},

		'And post is tapped': {
			'ios': [
				ios.tap(queries.ios.activity.postButton),
				ios.wait(3000)
			],
			'android': [
				android.tap(queries.android.activity.postButton),
				android.wait(3000)
			]
		},

		'And logout is tapped': {
			'ios': [
				ios.tap(queries.ios.activities.logoutButton)
			],
			'android': [
				android.pressBackButton(),
				android.wait(1000)
			]
		},
		'Then the Activities screen should be displayed' : {
			'ios': [
				ios.getPropertyValue(queries.ios.globals.navigationBar, 'topItem.title', function(result) {
					assert(result).equals('Activities');
				})
			],
			'android': [
				android.getPropertyValue({ class: 'android.widget.TextView', properties : { text: 'Activities' } }, 'text', function (result) {
					assert(result).equals('Activities');
				})
			]
		},
		'Then the activity should be posted': {
			'ios': [
				ios.wait(2000),
				ios.scrollToRow(queries.ios.activities.tableView, 0, 0),
				ios.getPropertyValue([{ class: 'UITableViewCell', index: 3 }, { class: 'UILabel', index: 2 }], 'text', function(value) {
					assert(value).equals('Hello World');
				})
			],
			'android': [
				android.wait(2000),
				android.getPropertyValue({ class: 'android.widget.TextView', properties: { 'text': 'Hello World' } }, 'text', function (value) {
					assert(value).equals('Hello World');
				})
			]
		},
		'Then the login screen should be displayed': {
			'ios': [
				ios.getPropertyValue(queries.ios.login.loginButton, 'text', function(value) {
					assert(value).equals('Login');
				})
			],
			'android': [
				android.getPropertyValue(queries.android.login.loginButton, 'text', function (value) {
					assert(value).equals('Login');
				})
			]
		}
	};

	/**
	 * Tests
	 * These are the tests that will be performed against the application.
	 */
	describe("Verify activities user interface works as expected", function() {
		test("Activities screen should display on login", function() {
			step('Given Friends is running');
			step('And is logged in');
			step('Then the Activities screen should be displayed');
		});

		test("Activities should be posted", function() {
			step('Given Friends is running');
			step('And is logged in');
			step('When add is tapped');
			step('And something on my mind is input');
			step('And post is tapped');
			step('Then the activity should be posted');
		});

		test("Logout returns to login screen", function() {
			step("Given Friends is running");
			step('And is logged in');
			step('And logout is tapped');
			step('Then the login screen should be displayed');
		});
	}, stepRepository);
});
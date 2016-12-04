<center>![Trognon icon](app/src/main/res/mipmap-xxxhdpi/dntf_logo.png "Trognon")</center>

# Trognon
Trognon is a small app to help you to remind the food you bought before it reaches the expiration date.
Food waste is something we should all avoid in order to reduce consumption and protect the environment.

Why Trognon ? Trognon is a french word meaning the core of a fruit, an apple for example. It can also
be used as an adjective to say "cute".

Nb: The project was originally called "Do Not Throw Food" (aka DNTF), but Trognon is more "trognon".

## App code
Here is an overview of the app mechanisms.

### Activities
- `OnBoarding` + `OnBoarding2`  
The on boarding activity is the first screen the user will see when starting the app for the
first time. It asks for user permission then redirects to the main activity. The redirection is
automatic when the on boarding is done. The activity does not have a sliding menu on the left neither
an action action bar on top.

- `Main`  
The main activity implements the logic for the scanner using the smartphone camera. It also
setups the alarm manager for notifications (see below for more details). The camera is paused automatcialy
if there is no activity for about 30s. The activity has a sliding menu on the left and an action bar on top.

- `About`  
The about activity is quite the same as the on boarding one, however it has a sliding menu on the left
and an action bar on top.

- `FoodList`  
The food list activity implements the logic for listing scanned products through a custom list layout.
Each element is clickable and point to the food details activity. The activity has a sliding menu
on the left and an action bar on top.

- `FoodDetails`  
The food details activity implements the logic to display informations about a scanned product. The activity
has a sliding menu on the left and an action bar on top.

### Permissions
Since newest versions of Android, it is not sufficient to set user permissions into the `Manifest` for some of them.
Required permissions are prompted during the on boarding, a special class `RequestUserPermission` manage the logic.
In this case the camera is the only permission we asked for. Also the app use some dependencies that ask more permissions,
we simply force the app to remove them into the `Manifest` as we don't need them.

### Notifications
Notifications are implemented into the  `NotificationsCenter` class. The logic is to setup a recurrent alarm
(every day) and send a notification if there is at least one product not green. The alarm is setup
during the creation of the main activity and is also enabled if the smartphone is reboot. This last logic
is implemented into the `BootCenter` class.

### Data
Products list is managed through the `SharedData` class. There is no database, we use the sharedPreferences in order
to keep data on the phone. The data is kept as String and manipulated thanks to GSON instance.
Moreover the `FoodApi` class allows to fetch data from the API and manipulate some of the fields of a product
object.

## Develop your own backend
Trognon is using the [open food facts api](http://world.openfoodfacts.org/data) in order to fetch informations for products.
If you want to develop your own backend, there is [an example](backend/) of code.

## Licensing
Trognon is licensed under the MIT License. See [LICENSE](LICENSE) for the full license text.

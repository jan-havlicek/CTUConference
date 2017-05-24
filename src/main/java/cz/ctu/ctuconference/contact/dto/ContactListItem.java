package cz.ctu.ctuconference.contact.dto;

/**
 * Data transfer object abstraction for contact in contact list.
 * It is empty abstract class. It is necessary to extend this abstract class and obtain proper
 * conversation entity information, that implements basic its basic interface containing id, name and conversation info.
 * When used in JSON, client side application can detect type by the name of the property in the contact list item
 * object.
 *
 * E.g. private list item obtain information about Friend, so it add "friend" property of this type, so by the name
 * of this property, application can discover, it is contact list item representing the Friend.
 *
 * Created by Nick Nemame on 25.09.2016.
 */
abstract public class ContactListItem {
}

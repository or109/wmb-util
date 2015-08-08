The WMB helper classes aims at making your Java transformation code easier. They do this by assuming some common patterns for message structures and transformations.

In general the helper classes are wrappers around the WMB `MbMessage` and `MbElement` classes. They provide strongly named and typed methods for creating and consuming messages. For example, instead of creating a RFH2 header by creating each MbElement of the correct type and value, you use the `RfhHeader` class which provides methods for the most common tasks done with RFH2 headers. If you need to do something the helper classes do not support, you can still use the WMB classes directly.

The helper classes all have static method for wrapping an existing element, creating a new and checking if a structure already exists.

Below are some simple examples which shows how to do some basic transformations.

## Creating an RFH header ##
Creating an RFH2 header requires that an MQMD header already exists, so we first create it using the `MqmdHeader` class.

```
// create outmessage from scratch, we normally do not want to copy to incoming message
MbMessage outMsg = new MbMessage();

MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly, outMsg);

// create MQMD for the out message
MqmdHeader mqmd = MqmdHeader.create(outMsg);

// create an RFH2 header
// creating the RFH2 header will automatically set the MQMD format correctly
Rfh2Header outRfh2 = Rfh2Header.create(outMsg);

// set a usr property
outRfh2.setStringProperty("usr", "foo", "bar");
```

## Reading an RFH2 header ##
```
MbMessage inMsg = inAssembly.getMessage();

Rfh2Header inRfh2 = Rfh2Header.wrap(inMsg, true);

// get a string value in the usr folder
String foo = inRfh2.getStringProperty("usr", "foo");
```

## Creating headers, message & attachment for the Email Node ##
The simplest way to send emails is to only create & configure the EmailPayload.
However, attachments and email server settings can be used by adding EmailAttachment & EmailDestination objects to the local environment.

```

// Create a new message from scratch
MbMessage outMessage = new MbMessage();

// Create a new assembly using input assembly and new message
MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly, outMessage);

// Wraps the local environemnt of the new assembly
LocalEnvironment localEnv = LocalEnvironment.wrap(outAssembly.getLocalEnvironment(), false);       

// Create the email payload
EmailPayload email = EmailPayload.create(outMessage);

// Create an attachment
EmailAttachment attachment = new EmailAttachment();

// Create an email destination header
EmailDestination destination = new EmailDestination();

// Sets the name of the attachment
attachment.setAttachmentContentName("text_file.txt");

// Sets the content (as text) of the attachment.
attachment.setAttachmentContentAsText("This is a textfile.");

// Sets the MIME content type of the attachment
attachment.setAttachmentContentType("text/plain");
		
// Sets the SMTP server (overrides Email Node settings)
destination.setSmtpServer("smtp.mydomain.com");
		
// Sets email addresses & subject
email.setTo("me@mydomain.com");
email.setFrom("wmb@mydomain.com");
email.setSubject("Email message from WMB");

// Sets email message body
email.setMessage("Hello world!");
		
// Add attachment & email destination header to the local environment
localEnv.addEmailAttachment(attachment);
localEnv.setEmailDestination(destination);
```

## Additional examples ##
You can find more examples in [SVN](http://wmb-util.googlecode.com/svn/wmb-util/trunk/src/examples/java/com/googlecode/wmbutil/messages/)
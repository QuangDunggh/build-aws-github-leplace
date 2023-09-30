<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body style="margin: 0; padding: 0;">

<div
    style="margin: 20px 20px; padding:5px; font-size: 10pt; line-height: 1.5; font-family: 'MS PGothic','Helvetica Neue', 'Helvetica', Arial, sans-serif;">
  <p>${userName} 様 </p>
  <p>いつも${serviceName}をご利用いただき、ありがとうございます。</p>

  <P>お客様のご注文を受け付けました。<br/>
    ※なお、このメールは注文の確定をお約束するものではありません。</P>
  <p>お客様のご注文が確定した時点で、「ご注文確定のお知らせ」というお知らせのメールをお送りいたします。</p>
  <p>「ご注文確定のお知らせ」のメールが届かない場合、以下の事項をご確認ください。</p>
  <P>
    １）以下のドメインの受信設定をご確認ください。
    <br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;★ ${emailDomain}

    <br/>
    ２）迷惑メールフォルダ等にメールが振り分けられていないかご確認ください。<br/>
    ３）１）２）をご確認の上、メールが届いていない場合には、${serviceName}ページ下部の「お問い合わせ」フォームからご連絡をお願いいたします。
  <P>
    本メールにお心当たりのない場合はお手数ですが、${serviceName}ページ下部の「お問い合わせ」フォームからご連絡をお願いいたします。
  </P>
  <p>
    <hr>
    <br/>
    ${serviceName}
  </p>

  <p style="line-height: 2"> URL <a href="${rootUrl}" style="text-decoration: none;">【${rootUrl}
      】 </a><br/>
    お問い合わせ先 <a href="${contactUrl}" style="text-decoration: none;">【${contactUrl}】</a><br/>

    <hr>
  </p>
  <p>
    ※このメールアドレスは配信専用となっております。本メールに返信していただきましても、お問い合わせにはお答えできませんのでご了承ください。
  </p>

</div>

</body>
</html>
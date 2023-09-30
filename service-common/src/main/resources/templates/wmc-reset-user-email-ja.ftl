<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body style="margin: 0; padding: 0;">

<div
    style="margin: 20px 20px; padding:5px; font-size: 10pt; line-height: 1.5; font-family: 'MS PGothic','Helvetica Neue', 'Helvetica', Arial, sans-serif;">
  <p>
      <i style="color:black;">${userName}</i>様
  </p>
  <p>
    いつも ${serviceName} をご利用いただき、ありがとうございます。
  </p>
  <p>
    メールアドレスの変更に必要なURLをお送りします。
    <br/>
    メールアドレスを変更する場合は下記のURLにアクセスし変更を完了してください。
  </p>
  <p>
    【URL】<a href="${link}"><i style="color:blue;">${link}</i><a>
  </p>
  <P>※1時間以内にご変更完了まで行われない場合、URLは無効になります。</P>
  <hr>

  <p style="line-height: 2">【お問い合わせ先】<a href="${contactUrl}"
       style="text-decoration: none;"><i style="color:blue;">${contactUrl}</i></a></p>

  <hr>

  <p>
    ※このメールアドレスは配信専用となっております。本メールに返信いただいても、お問い合わせにはお答えできませんのでご了承ください。
  </p>

</div>

</body>
</html>
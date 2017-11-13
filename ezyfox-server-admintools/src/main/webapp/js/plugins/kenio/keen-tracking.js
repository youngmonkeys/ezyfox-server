!function(name,path,ctx){
  var latest,prev=name!=='Keen'&&window.Keen?window.Keen:false;ctx[name]=ctx[name]||{ready:function(fn){var h=document.getElementsByTagName('head')[0],s=document.createElement('script'),w=window,loaded;s.onload=s.onerror=s.onreadystatechange=function(){if((s.readyState&&!(/^c|loade/.test(s.readyState)))||loaded){return}s.onload=s.onreadystatechange=null;loaded=1;latest=w.Keen;if(prev){w.Keen=prev}else{try{delete w.Keen}catch(e){w.Keen=void 0}}ctx[name]=latest;ctx[name].ready(fn)};s.async=1;s.src=path;h.parentNode.insertBefore(s,h)}}
}('KeenTracking','https://d26b395fwzu5fz.cloudfront.net/keen-tracking-1.0.3.min.js',this);

KeenTracking.ready(function(){

  // Create a new client instance
  var client = new KeenTracking({
    projectId: '59a513d8c9e77c0001bc205e',
    writeKey: '66825E765A59DF17F2D33D31DEA791062C61301C802F231C1945909FCBD68B060FBB3851C0CCB67561C29F2757616B277F35351A2C803095C955F1E53CACEC11086CF3D39B3AA5ABD06AD5AF9D47C3E0EBF65C2B07A35D93C3DA408C79AF6385'
  });

  var sessionCookie = KeenTracking.utils.cookie('keen-cookie');
  if (!sessionCookie.get('user_id')) {
    sessionCookie.set('user_id', KeenTracking.helpers.getUniqueId());
  }

  var sessionTimer = KeenTracking.utils.timer();
  sessionTimer.start();

  KeenTracking.listenTo({
      'click .nav a': function(e){
          // 500ms to record an event
          client.recordEvent('leave page');
      }
  });

  // DATA MODEL
  client.extendEvents(function(){
      return {
          page: {
              title: document.title,
              url: document.location.href
              // info: {} (add-on)
          },
          referrer: {
              url: document.referrer
              // info: {} (add-on)
          },
          tech: {
              browser: KeenTracking.helpers.getBrowserProfile(),
              // info: {} (add-on)
              ip: '${keen.ip}',
              ua: '${keen.user_agent}'
          },
          time: KeenTracking.helpers.getDatetimeIndex(),
          visitor: {
              id: sessionCookie.get('user_id'),
              time_on_page: sessionTimer.value()
          },
          // geo: {} (add-on)
          keen: {
              timestamp: new Date().toISOString(),
              addons: [
                  {
                      name: 'keen:ip_to_geo',
                      input: {
                          ip: 'tech.ip'
                      },
                      output: 'geo'
                  },
                  {
                      name: 'keen:ua_parser',
                      input: {
                          ua_string: 'tech.ua'
                      },
                      output: 'tech.info'
                  },
                  {
                      name: 'keen:url_parser',
                      input: {
                          url: 'page.url'
                      },
                      output: 'page.info'
                  },
                  {
                      name: 'keen:referrer_parser',
                      input: {
                          page_url: 'page.url',
                          referrer_url: 'referrer.url'
                      },
                      output: 'referrer.info'
                  }
              ]
          }
      };
  });

  client.recordEvent('pageview');

});

# Lobby


### Config example
```
# Spawn coords storage
spawn:
  x: 0.5
  y: 69
  z: 0.5
  yaw: 180
  pitch: 0
  world: world

options:
  # the world the lobby is in
  world: world
  
  # Stop plants from growing
  # Vines, Wheat, Sugarcane, etc
  disable-plantGrowth: true
  
  # Disables sever weather in specified world
  disable-weather: true
  
  # Locks daylight cycle in specified world
  # to-do
  disable-dayLightCycle: true
  
  # Disables mob natural mob spawing in specified world
  # mobs can still be spawned using eggs or with commands
  disable-mobSpawing: true
  
  build:
    # Stop players from building
    # Set bypass permission and no permission message
    disable-block-break: true
    block-break-msg: "&f[&6Lobby&f] You're not allowed to do that here!"
    break-bypass-permission: "lobby.breakblocks"
    
    # Stop players from building
    # Set bypass permission and no permission message
    disable-block-place: true
    block-place-msg: "&f[&6Lobby&f]You're not allowed to do that here!"
    place-bypass-permission: "lobby.placeblocks"
    
tablist:
  enabled: true
  header:
    - "header"
    - "second line"
    - "%player%"
  footer:
    - "footer"
    - "second line"
    - "third line"
    
servers:
  factions:
    ip: play.hypixel.net
    port: 25565
  skyblock:
    ip: us.mineplex.com
    port: 25565
  some-other-server:
    ip: ruthlesspvp.us
    port: 25565
```

### Commands

- /lobby  - `lobby.help` _Displays all Lobby commands in chat_
- /lobby reload \<selector | hideplayers | config> - `lobby.reload` _Reloads specified config file_
- /lobby setspawn - `lobby.setspawn` _Sets lobby spawn point_
- /lobby spawn - `lobby.spawn` _Teleports player to spawn_



# NPC

### Config example
```
# Interact cool down
cooldown: 2

 # NPC name
 mooselk:
 
    # Internal NPC id (must match name)
    id: mooselk
    
    # SkinIds can be obtained from MineSkin.org
    skin: 2100230944
    
    # Text shown above NPC
    holotext:
    - '&3&lSkyBlock'
    - '&eClick to join!'
    
    # Message and command
    # Set to none for no command or no message
    message:
    - '&f[&6Lobby&f] Sending you to server &6SkyBlock.'
    - 'Optional extra line'
    
    # NPC equipment
    # Format: region:item:enchanted
    # Regions: helmet, chestplate, leggings, boots, hand, (1.9+) offhand
    equipment:
     - 'helmet:IRON_HELMET:true'
     - 'boots:DIAMOND_BOOTS:true'
     - 'hand:TNT'
    
    # Server info
    server:
      live-player-count: true
      
      # Bungee server player is sent to onInteract
      # Server for live player count (Defined in config.yml)
      server-name: skyblock
    
    # NPCS location
    location:
      x: -0.5
      y: 63
      z: -14.5
      yaw: -5
      pitch: 0
```

### Commands

- /npc help  - `lobby.npc.help` _Displays all NPC commands in chat_
- /npc create \<npc_name> \<skinId> - `lobby.npc.create`   _Creates NPC at players location_
- /npc move \<npc_name> - `lobby.npc.move`  _Moves NPC to players location_
- /npc delete \<npc_name> - `lobby.npc.delete`  _Deletes specified NPC_
- /npc reload - `lobby.npc.reload`  _Reloads ALL NPCs_



# Portals

### Commands
- /portal help  - `lobby.portal.help` _Displays all Portal commands in chat_
- /portal wand - `lobby.portal.wand` _Special tool used to select portal region_
- /portal clear - `lobby.portal.clear` _Clears current portal selection_
- /portal create \<name> \<server>  - `lobby.portal.create` _Creates portal in selected region_
- /portal delete \<name> - `lobby.portal.delete` _Deletes specified portal_
- /portal reload - `lobby.portal.reload` _Reloads ALL portals_



# Server Selector

[Full Server-selector config example](https://github.com/Mooselk/All-In-One-Lobby/blob/master/src/selector.yml) - 
[Example menu](https://github.com/Mooselk/All-In-One-Lobby/blob/master/Menu-example.png)

### Config
```
selector:
  options:
    
    # Enable / disable selector item
    enabled: true
    
    # Number of menu rows
    rows: 6
    
    # Selector item
    material: "COMPASS"
    
    # Item display name
    item-name: "&4Server-Selector &8(&7Right-click&8)"
    
    # Set selector lore here
    lore:
      - "&8Selects server"
    
    # Name at the top of menu
    name: "&4&lExample &8&l>> &cServers"
    
    # Slot item appears in inventory
    slot: 0
    
  # Inventory slot
  19:
  
    # If server status is equal to online, this info is shown.
    online:
    
      # Item name
      name: '&8* &cFactions Hell &7(HCF) &8*'
      
      # Item material
      material: "TNT"
      
      # Item data
      byte: 0
      
      # Item appears enchanted
      enchanted: false
      
      # Item Lore
      lore:
        - '&8&m&l|-&r &cDescription'
        - '&8&m&l|&r &7&o"Hardcore factions when you'
        - '&8&m&l|&r &7&odie you are banned for'
        - '&8&m&l|&r &7&oset about of time!"'
        - '&8&m&l|-&r &cStatistics'
        - '&8&m&l|&r &fSupported: &71.8.8 - 1.13'
        - '&8&m&l|&r &fRecommended: &71.8.8 - 1.8.9'
        - '&8&m&l|&r &fONLINE: &7%online%/%max%'
        - '&8&m&l|&r &fMAP: &7#1'
        - '&8&m&l|&r &8- &aONLINE &8-'
        - '&8&m&l|-&r'
    # If server status is equal to offline, this info is shown.
    offline:
    
      # Item-name
      name: '&8* &cFactions Hell &7(HCF) &8*'
      
      # Material
      material: "WOOL"
      
      # Material data (Red wool)
      byte: 14
      
      # Item appears enchanted
      enchanted: false
      
      # Item lore
      lore:
        - " "
        - "&7- &cOFFLINE &7-"
        - " "
      
    # Server ping settings
    server:
      
      # ID can by anything aslong as it's unique
      server-id: factions
      
      # Ping server for live info
      ping-server: true
      
      # IP to get data from 
      ip: me.hypixel.net
      
      # Port
      port: 25565
      
      # false for local host, true for external server
      external-query: true
      
      # Ping timeout
      ping-timeout: 10
      
    # Server to connect to
    server: factions
    
    # Message sent on click
    message: "&f[&6Lobby&f] Sending you to server '&6factions&f'!"
    
    
    # Example deco item
    # Can be used to display a message or to fill the rest of the inventory
    45:
    
    # Must be set to true to ignore all normal options
    decoration: true
    name: " "
    material: "STAINED_GLASS_PANE"
    byte: 0
```


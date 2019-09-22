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
    message: '&f[&6Lobby&f] Sending you to server &6SkyBlock.'
    command: server skyblock
    
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

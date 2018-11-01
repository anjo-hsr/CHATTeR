import React from 'react';
import Avatar from 'avataaars';

import {avatar as avatarProperties} from '../../defaults/defaults';

export default {
  getOwnAvatar() {
    let avatar = avatarProperties.ownAvatar;
    return (
      <Avatar
        style={{width: avatarProperties.avatarSize, height: avatarProperties.avatarSize}}
        avatarStyle={avatar.avatarStyle}
        topType={avatar.topType}
        accessoriesType={avatar.accessoriesType}
        hairColor={avatar.hairColor}
        facialHairType={avatar.facialHairType}
        clotheType={avatar.clotheType}
        clotheColor={avatar.clotheColor}
        eyeType={avatar.eyeType}
        eyebrowType={avatar.eyebrowType}
        mouthType={avatar.mouthType}
        skinColor={avatar.skinColor}
      />
    );
  },

  getOtherAvatar(avatar) {
    return (
      <Avatar
        style={{width: '4rem', height: '4rem'}}
        avatarStyle={avatar.avatarStyle}
        topType={avatar.topType}
        accessoriesType={avatar.accessoriesType}
        hairColor={avatar.hairColor}
        facialHairType={avatar.facialHairType}
        clotheType={avatar.clotheType}
        clotheColor={avatar.clotheColor}
        eyeType={avatar.eyeType}
        eyebrowType={avatar.eyebrowType}
        mouthType={avatar.mouthType}
        skinColor={avatar.skinColor}
      />
    );
  }
};
